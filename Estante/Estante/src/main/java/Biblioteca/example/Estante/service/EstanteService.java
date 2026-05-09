package Biblioteca.example.Estante.service;
import Biblioteca.example.Estante.client.LibroClient;
import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.model.Estante;
import Biblioteca.example.Estante.repository.EstanteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
            throw new RuntimeException("El libro no existe con id: " + libroId);
        } catch (FeignException ex) {
            throw new RuntimeException("No se puede contactar con el servicio de libros: " + ex.getMessage());
        }
    }
    public List<EstanteResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<EstanteResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public EstanteResponseDTO guardar(EstanteRequestDTO dto) {
        validarLibro(dto.getLibroId());
        Estante e = new Estante(null, dto.getNumero(), dto.getNivel(), dto.getPasillo(), dto.getLibroId());
        return mapToDTO(repository.save(e));
    }
    public EstanteResponseDTO actualizar(Long id, EstanteRequestDTO dto) {
        Estante e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estante no encontrado con id: " + id));
        validarLibro(dto.getLibroId());
        e.setNumero(dto.getNumero());
        e.setNivel(dto.getNivel());
        e.setPasillo(dto.getPasillo());
        e.setLibroId(dto.getLibroId());
        return mapToDTO(repository.save(e));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Estante no encontrado con id: " + id);
        repository.deleteById(id);
    }
}
