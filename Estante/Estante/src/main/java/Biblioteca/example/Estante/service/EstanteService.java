package Biblioteca.example.Estante.service;
import Biblioteca.example.Estante.client.LibroClient;
import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.model.Estante;
import Biblioteca.example.Estante.repository.EstanteRepository;
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
public class EstanteService {
    private final EstanteRepository repository;
    private final LibroClient libroClient;

    private EstanteResponseDTO mapToDTO(Estante e) {
        return new EstanteResponseDTO(e.getId(), e.getNumero(), e.getNivel(), e.getPasillo(), e.getLibroId());
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

    public List<EstanteResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los estantes");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<EstanteResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando estante con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public EstanteResponseDTO guardar(EstanteRequestDTO dto) {
        log.info("Creando nuevo estante para libro id: {}", dto.getLibroId());
        validarLibro(dto.getLibroId());
        Estante e = new Estante(null, dto.getNumero(), dto.getNivel(), dto.getPasillo(), dto.getLibroId());
        Estante saved = repository.save(e);
        log.info("Estante creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public EstanteResponseDTO actualizar(Long id, EstanteRequestDTO dto) {
        log.info("Actualizando estante con id: {}", id);
        Estante e = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Estante no encontrado con id: {}", id);
                    return new RuntimeException("Estante no encontrado con id: " + id);
                });
        validarLibro(dto.getLibroId());
        e.setNumero(dto.getNumero());
        e.setNivel(dto.getNivel());
        e.setPasillo(dto.getPasillo());
        e.setLibroId(dto.getLibroId());
        Estante updated = repository.save(e);
        log.info("Estante actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando estante con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar estante inexistente con id: {}", id);
            throw new RuntimeException("Estante no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Estante eliminado exitosamente con id: {}", id);
    }
}
