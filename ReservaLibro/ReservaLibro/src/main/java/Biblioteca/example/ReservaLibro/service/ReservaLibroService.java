package Biblioteca.example.ReservaLibro.service;
import Biblioteca.example.ReservaLibro.client.LibroClient;
import Biblioteca.example.ReservaLibro.client.MultasClient;
import Biblioteca.example.ReservaLibro.client.UserClient;
import Biblioteca.example.ReservaLibro.dto.EstadoMultasDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroRequestDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroResponseDTO;
import Biblioteca.example.ReservaLibro.model.ReservaLibro;
import Biblioteca.example.ReservaLibro.repository.ReservaLibroRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ReservaLibroService {
    private final ReservaLibroRepository repository;
    private final LibroClient libroClient;
    private final UserClient userClient;
    private final MultasClient multasClient;
    private ReservaLibroResponseDTO mapToDTO(ReservaLibro r, String aviso) {
        return new ReservaLibroResponseDTO(r.getId(), r.getUserId(), r.getLibroId(),
                r.getFechaReserva(), r.getEstadoReserva(), aviso);
    }
    private ReservaLibroResponseDTO mapToDTO(ReservaLibro r) {
        return mapToDTO(r, null);
    }
    private void validarLibro(Long libroId) {
        try { libroClient.obtenerPorId(libroId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El libro no existe con id: " + libroId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de libros."); }
    }
    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El usuario no existe con id: " + userId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de usuarios."); }
    }
    private String validarMultas(Long userId) {
        try {
            EstadoMultasDTO estado = multasClient.obtenerEstado(userId);
            if (!estado.isPuedeReservar()) {
                throw new RuntimeException(estado.getAviso());
            }
            return estado.getTotalMultas() > 0 ? estado.getAviso() : null;
        } catch (FeignException ex) {
            throw new RuntimeException("No se puede contactar con el servicio de multas.");
        }
    }
    public List<ReservaLibroResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<ReservaLibroResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public ReservaLibroResponseDTO guardar(ReservaLibroRequestDTO dto) {
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        String aviso = validarMultas(dto.getUserId());
        if ("ACTIVA".equals(dto.getEstadoReserva())) {
            repository.findByLibroIdAndEstadoReserva(dto.getLibroId(), "ACTIVA")
                    .ifPresent(r -> { throw new RuntimeException("El libro ya tiene una reserva activa."); });
        }
        ReservaLibro r = new ReservaLibro(null, dto.getUserId(), dto.getLibroId(),
                dto.getFechaReserva(), dto.getEstadoReserva());
        return mapToDTO(repository.save(r), aviso);
    }
    public ReservaLibroResponseDTO actualizar(Long id, ReservaLibroRequestDTO dto) {
        ReservaLibro r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        r.setUserId(dto.getUserId());
        r.setLibroId(dto.getLibroId());
        r.setFechaReserva(dto.getFechaReserva());
        r.setEstadoReserva(dto.getEstadoReserva());
        return mapToDTO(repository.save(r));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Reserva no encontrada con id: " + id);
        repository.deleteById(id);
    }
}
