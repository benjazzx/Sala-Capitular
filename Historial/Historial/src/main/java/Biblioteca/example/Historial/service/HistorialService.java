package Biblioteca.example.Historial.service;
import Biblioteca.example.Historial.client.EstadoClient;
import Biblioteca.example.Historial.client.LibroClient;
import Biblioteca.example.Historial.client.UserClient;
import Biblioteca.example.Historial.dto.HistorialRequestDTO;
import Biblioteca.example.Historial.dto.HistorialResponseDTO;
import Biblioteca.example.Historial.model.Historial;
import Biblioteca.example.Historial.repository.HistorialRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class HistorialService {
    private final HistorialRepository repository;
    private final LibroClient libroClient;
    private final UserClient userClient;
    private final EstadoClient estadoClient;
    private HistorialResponseDTO mapToDTO(Historial h) {
        return new HistorialResponseDTO(h.getId(), h.getUserId(), h.getLibroId(),
                h.getFechaPrestamo(), h.getFechaDevolucion(), h.getEstadoId());
    }
    private void validar(Long libroId, Long userId, Long estadoId) {
        try { libroClient.obtenerPorId(libroId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El libro no existe con id: " + libroId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de libros."); }
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El usuario no existe con id: " + userId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de usuarios."); }
        try { estadoClient.obtenerPorId(estadoId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El estado no existe con id: " + estadoId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de estados."); }
    }
    public List<HistorialResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<HistorialResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public HistorialResponseDTO guardar(HistorialRequestDTO dto) {
        validar(dto.getLibroId(), dto.getUserId(), dto.getEstadoId());
        Historial h = new Historial(null, dto.getUserId(), dto.getLibroId(),
                dto.getFechaPrestamo(), dto.getFechaDevolucion(), dto.getEstadoId());
        return mapToDTO(repository.save(h));
    }
    public HistorialResponseDTO actualizar(Long id, HistorialRequestDTO dto) {
        Historial h = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con id: " + id));
        validar(dto.getLibroId(), dto.getUserId(), dto.getEstadoId());
        h.setUserId(dto.getUserId());
        h.setLibroId(dto.getLibroId());
        h.setFechaPrestamo(dto.getFechaPrestamo());
        h.setFechaDevolucion(dto.getFechaDevolucion());
        h.setEstadoId(dto.getEstadoId());
        return mapToDTO(repository.save(h));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Historial no encontrado con id: " + id);
        repository.deleteById(id);
    }
}
