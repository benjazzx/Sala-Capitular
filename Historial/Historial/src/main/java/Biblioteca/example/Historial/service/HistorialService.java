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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
        catch (FeignException.NotFound ex) {
            log.warn("Libro no encontrado con id: {}", libroId);
            throw new RuntimeException("El libro no existe con id: " + libroId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de libros: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de libros.");
        }
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) {
            log.warn("Usuario no encontrado con id: {}", userId);
            throw new RuntimeException("El usuario no existe con id: " + userId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de usuarios: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de usuarios.");
        }
        try { estadoClient.obtenerPorId(estadoId); }
        catch (FeignException.NotFound ex) {
            log.warn("Estado no encontrado con id: {}", estadoId);
            throw new RuntimeException("El estado no existe con id: " + estadoId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de estados: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de estados.");
        }
    }

    public List<HistorialResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los historiales");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<HistorialResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando historial con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public HistorialResponseDTO guardar(HistorialRequestDTO dto) {
        log.info("Registrando préstamo para usuario id: {} y libro id: {}", dto.getUserId(), dto.getLibroId());
        validar(dto.getLibroId(), dto.getUserId(), dto.getEstadoId());
        Historial h = new Historial(null, dto.getUserId(), dto.getLibroId(),
                dto.getFechaPrestamo(), dto.getFechaDevolucion(), dto.getEstadoId());
        Historial saved = repository.save(h);
        log.info("Historial creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public HistorialResponseDTO actualizar(Long id, HistorialRequestDTO dto) {
        log.info("Actualizando historial con id: {}", id);
        Historial h = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Historial no encontrado con id: {}", id);
                    return new RuntimeException("Historial no encontrado con id: " + id);
                });
        validar(dto.getLibroId(), dto.getUserId(), dto.getEstadoId());
        h.setUserId(dto.getUserId());
        h.setLibroId(dto.getLibroId());
        h.setFechaPrestamo(dto.getFechaPrestamo());
        h.setFechaDevolucion(dto.getFechaDevolucion());
        h.setEstadoId(dto.getEstadoId());
        Historial updated = repository.save(h);
        log.info("Historial actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando historial con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar historial inexistente con id: {}", id);
            throw new RuntimeException("Historial no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Historial eliminado exitosamente con id: {}", id);
    }
}
