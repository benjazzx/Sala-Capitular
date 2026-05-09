package Biblioteca.example.Multas.service;
import Biblioteca.example.Multas.client.HistorialClient;
import Biblioteca.example.Multas.client.UserClient;
import Biblioteca.example.Multas.dto.MultaRequestDTO;
import Biblioteca.example.Multas.dto.MultaResponseDTO;
import Biblioteca.example.Multas.model.Multa;
import Biblioteca.example.Multas.repository.MultaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class MultaService {
    private final MultaRepository repository;
    private final UserClient userClient;
    private final HistorialClient historialClient;
    private MultaResponseDTO mapToDTO(Multa m) {
        return new MultaResponseDTO(m.getId(), m.getUserId(), m.getHistorialId(),
                m.getDescripcion(), m.getFecha(), m.getCantidad());
    }
    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El usuario no existe con id: " + userId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de usuarios."); }
    }
    private void validarHistorial(Long historialId) {
        try { historialClient.obtenerPorId(historialId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El historial no existe con id: " + historialId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de historial."); }
    }
    public List<MultaResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<MultaResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public List<MultaResponseDTO> obtenerPorUsuario(Long userId) {
        return repository.findByUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public boolean puedeReservar(Long userId) {
        Integer total = repository.sumCantidadByUserId(userId);
        return total <= 3;
    }
    public MultaResponseDTO guardar(MultaRequestDTO dto) {
        validarUser(dto.getUserId());
        validarHistorial(dto.getHistorialId());
        Multa m = new Multa(null, dto.getUserId(), dto.getHistorialId(),
                dto.getDescripcion(), dto.getFecha(), dto.getCantidad());
        return mapToDTO(repository.save(m));
    }
    public MultaResponseDTO actualizar(Long id, MultaRequestDTO dto) {
        Multa m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con id: " + id));
        validarUser(dto.getUserId());
        validarHistorial(dto.getHistorialId());
        m.setUserId(dto.getUserId());
        m.setHistorialId(dto.getHistorialId());
        m.setDescripcion(dto.getDescripcion());
        m.setFecha(dto.getFecha());
        m.setCantidad(dto.getCantidad());
        return mapToDTO(repository.save(m));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Multa no encontrada con id: " + id);
        repository.deleteById(id);
    }
}
