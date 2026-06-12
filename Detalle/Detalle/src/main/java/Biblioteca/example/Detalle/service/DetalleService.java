package Biblioteca.example.Detalle.service;
import Biblioteca.example.Detalle.client.HistorialClient;
import Biblioteca.example.Detalle.client.LibroClient;
import Biblioteca.example.Detalle.dto.DetalleRequestDTO;
import Biblioteca.example.Detalle.dto.DetalleResponseDTO;
import Biblioteca.example.Detalle.model.Detalle;
import Biblioteca.example.Detalle.repository.DetalleRepository;
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
public class DetalleService {
    private final DetalleRepository repository;
    private final HistorialClient historialClient;
    private final LibroClient libroClient;

    private DetalleResponseDTO mapToDTO(Detalle d) {
        return new DetalleResponseDTO(d.getId(), d.getHistorialId(), d.getLibroId(), d.getObservacion());
    }

    private void validarHistorial(Long historialId) {
        try {
            historialClient.obtenerPorId(historialId);
        } catch (FeignException.NotFound ex) {
            log.warn("Historial no encontrado con id: {}", historialId);
            throw new RuntimeException("El historial no existe con id: " + historialId);
        } catch (FeignException ex) {
            log.error("Error al contactar servicio de historial: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de historial: " + ex.getMessage());
        }
    }

    private void validarLibro(Long libroId) {
        try {
            libroClient.obtenerPorId(libroId);
        } catch (FeignException.NotFound ex) {
            log.warn("Libro no encontrado con id: {}", libroId);
            throw new RuntimeException("El libro no existe con id: " + libroId);
        } catch (FeignException ex) {
            log.error("Error al contactar servicio de libros: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de libros: " + ex.getMessage());
        }
    }

    public List<DetalleResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los detalles");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<DetalleResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando detalle con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public DetalleResponseDTO guardar(DetalleRequestDTO dto) {
        log.info("Creando detalle para historial id: {} y libro id: {}", dto.getHistorialId(), dto.getLibroId());
        validarHistorial(dto.getHistorialId());
        validarLibro(dto.getLibroId());
        Detalle d = new Detalle(null, dto.getHistorialId(), dto.getLibroId(), dto.getObservacion());
        Detalle saved = repository.save(d);
        log.info("Detalle creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public DetalleResponseDTO actualizar(Long id, DetalleRequestDTO dto) {
        log.info("Actualizando detalle con id: {}", id);
        Detalle d = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Detalle no encontrado con id: {}", id);
                    return new RuntimeException("Detalle no encontrado con id: " + id);
                });
        validarHistorial(dto.getHistorialId());
        validarLibro(dto.getLibroId());
        d.setHistorialId(dto.getHistorialId());
        d.setLibroId(dto.getLibroId());
        d.setObservacion(dto.getObservacion());
        Detalle updated = repository.save(d);
        log.info("Detalle actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando detalle con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar detalle inexistente con id: {}", id);
            throw new RuntimeException("Detalle no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Detalle eliminado exitosamente con id: {}", id);
    }
}
