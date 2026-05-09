package Biblioteca.example.Detalle.service;
import Biblioteca.example.Detalle.client.HistorialClient;
import Biblioteca.example.Detalle.client.LibroClient;
import Biblioteca.example.Detalle.dto.DetalleRequestDTO;
import Biblioteca.example.Detalle.dto.DetalleResponseDTO;
import Biblioteca.example.Detalle.model.Detalle;
import Biblioteca.example.Detalle.repository.DetalleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
            throw new RuntimeException("El historial no existe con id: " + historialId);
        } catch (FeignException ex) {
            throw new RuntimeException("No se puede contactar con el servicio de historial: " + ex.getMessage());
        }
    }
    private void validarLibro(Long libroId) {
        try {
            libroClient.obtenerPorId(libroId);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El libro no existe con id: " + libroId);
        } catch (FeignException ex) {
            throw new RuntimeException("No se puede contactar con el servicio de libros: " + ex.getMessage());
        }
    }
    public List<DetalleResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<DetalleResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public DetalleResponseDTO guardar(DetalleRequestDTO dto) {
        validarHistorial(dto.getHistorialId());
        validarLibro(dto.getLibroId());
        Detalle d = new Detalle(null, dto.getHistorialId(), dto.getLibroId(), dto.getObservacion());
        return mapToDTO(repository.save(d));
    }
    public DetalleResponseDTO actualizar(Long id, DetalleRequestDTO dto) {
        Detalle d = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado con id: " + id));
        validarHistorial(dto.getHistorialId());
        validarLibro(dto.getLibroId());
        d.setHistorialId(dto.getHistorialId());
        d.setLibroId(dto.getLibroId());
        d.setObservacion(dto.getObservacion());
        return mapToDTO(repository.save(d));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Detalle no encontrado con id: " + id);
        repository.deleteById(id);
    }
}
