package Biblioteca.example.Catalogo.service;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.model.Catalogo;
import Biblioteca.example.Catalogo.repository.CatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final CatalogoRepository repository;

    private CatalogoResponseDTO mapToDTO(Catalogo catalogo) {
        return new CatalogoResponseDTO(catalogo.getId(), catalogo.getNombre(), catalogo.getDescripcion());
    }

    public List<CatalogoResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<CatalogoResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    public CatalogoResponseDTO guardar(CatalogoRequestDTO dto) {
        Catalogo catalogo = new Catalogo(null, dto.getNombre(), dto.getDescripcion());
        return mapToDTO(repository.save(catalogo));
    }

    public CatalogoResponseDTO actualizar(Long id, CatalogoRequestDTO dto) {
        Catalogo catalogo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catálogo no encontrado con id: " + id));
        catalogo.setNombre(dto.getNombre());
        catalogo.setDescripcion(dto.getDescripcion());
        return mapToDTO(repository.save(catalogo));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Catálogo no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
