package Biblioteca.example.ResenaLibro.service;
import Biblioteca.example.ResenaLibro.client.LibroClient;
import Biblioteca.example.ResenaLibro.client.UserClient;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.model.ResenaLibro;
import Biblioteca.example.ResenaLibro.repository.ResenaLibroRepository;
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
public class ResenaLibroService {
    private final ResenaLibroRepository repository;
    private final LibroClient libroClient;
    private final UserClient userClient;

    private ResenaLibroResponseDTO mapToDTO(ResenaLibro r) {
        return new ResenaLibroResponseDTO(r.getId(), r.getUserId(), r.getLibroId(),
                r.getCalificacion(), r.getComentario(), r.getFecha());
    }

    private void validarLibro(Long libroId) {
        try { libroClient.obtenerPorId(libroId); }
        catch (FeignException.NotFound ex) {
            log.warn("Libro no encontrado con id: {}", libroId);
            throw new RuntimeException("El libro no existe con id: " + libroId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de libros: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de libros: " + ex.getMessage());
        }
    }

    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) {
            log.warn("Usuario no encontrado con id: {}", userId);
            throw new RuntimeException("El usuario no existe con id: " + userId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de usuarios: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de usuarios: " + ex.getMessage());
        }
    }

    public List<ResenaLibroResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las reseñas");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<ResenaLibroResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando reseña con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public List<ResenaLibroResponseDTO> obtenerPorLibro(Long libroId) {
        log.info("Obteniendo reseñas del libro con id: {}", libroId);
        return repository.findByLibroId(libroId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ResenaLibroResponseDTO guardar(ResenaLibroRequestDTO dto) {
        log.info("Creando reseña de usuario id: {} para libro id: {} con calificación: {}", dto.getUserId(), dto.getLibroId(), dto.getCalificacion());
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        ResenaLibro r = new ResenaLibro(null, dto.getUserId(), dto.getLibroId(),
                dto.getCalificacion(), dto.getComentario(), dto.getFecha());
        ResenaLibro saved = repository.save(r);
        log.info("Reseña creada exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public ResenaLibroResponseDTO actualizar(Long id, ResenaLibroRequestDTO dto) {
        log.info("Actualizando reseña con id: {}", id);
        ResenaLibro r = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reseña no encontrada con id: {}", id);
                    return new RuntimeException("Reseña no encontrada con id: " + id);
                });
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        r.setUserId(dto.getUserId());
        r.setLibroId(dto.getLibroId());
        r.setCalificacion(dto.getCalificacion());
        r.setComentario(dto.getComentario());
        r.setFecha(dto.getFecha());
        ResenaLibro updated = repository.save(r);
        log.info("Reseña actualizada exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando reseña con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar reseña inexistente con id: {}", id);
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Reseña eliminada exitosamente con id: {}", id);
    }
}
