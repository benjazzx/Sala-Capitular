package Biblioteca.example.ResenaLibro.service;
import Biblioteca.example.ResenaLibro.client.LibroClient;
import Biblioteca.example.ResenaLibro.client.UserClient;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.model.ResenaLibro;
import Biblioteca.example.ResenaLibro.repository.ResenaLibroRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        catch (FeignException.NotFound ex) { throw new RuntimeException("El libro no existe con id: " + libroId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de libros: " + ex.getMessage()); }
    }
    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El usuario no existe con id: " + userId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de usuarios: " + ex.getMessage()); }
    }
    public List<ResenaLibroResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<ResenaLibroResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public List<ResenaLibroResponseDTO> obtenerPorLibro(Long libroId) {
        return repository.findByLibroId(libroId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public ResenaLibroResponseDTO guardar(ResenaLibroRequestDTO dto) {
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        ResenaLibro r = new ResenaLibro(null, dto.getUserId(), dto.getLibroId(),
                dto.getCalificacion(), dto.getComentario(), dto.getFecha());
        return mapToDTO(repository.save(r));
    }
    public ResenaLibroResponseDTO actualizar(Long id, ResenaLibroRequestDTO dto) {
        ResenaLibro r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con id: " + id));
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        r.setUserId(dto.getUserId());
        r.setLibroId(dto.getLibroId());
        r.setCalificacion(dto.getCalificacion());
        r.setComentario(dto.getComentario());
        r.setFecha(dto.getFecha());
        return mapToDTO(repository.save(r));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Reseña no encontrada con id: " + id);
        repository.deleteById(id);
    }
}
