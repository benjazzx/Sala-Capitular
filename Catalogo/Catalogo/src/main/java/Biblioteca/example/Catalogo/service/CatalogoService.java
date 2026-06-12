package Biblioteca.example.Catalogo.service;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.model.Catalogo;
import Biblioteca.example.Catalogo.repository.CatalogoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final CatalogoRepository repository;

    private CatalogoResponseDTO mapToDTO(Catalogo catalogo) {
        return new CatalogoResponseDTO(catalogo.getId(), catalogo.getNombre(), catalogo.getDescripcion());
    }

    public List<CatalogoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los catálogos");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<CatalogoResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando catálogo con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public CatalogoResponseDTO guardar(CatalogoRequestDTO dto) {
        log.info("Creando nuevo catálogo: {}", dto.getNombre());
        Catalogo catalogo = new Catalogo(null, dto.getNombre(), dto.getDescripcion());
        Catalogo saved = repository.save(catalogo);
        log.info("Catálogo creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public CatalogoResponseDTO actualizar(Long id, CatalogoRequestDTO dto) {
        log.info("Actualizando catálogo con id: {}", id);
        Catalogo catalogo = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Catálogo no encontrado con id: {}", id);
                    return new RuntimeException("Catálogo no encontrado con id: " + id);
                });
        catalogo.setNombre(dto.getNombre());
        catalogo.setDescripcion(dto.getDescripcion());
        Catalogo updated = repository.save(catalogo);
        log.info("Catálogo actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando catálogo con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar catálogo inexistente con id: {}", id);
            throw new RuntimeException("Catálogo no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Catálogo eliminado exitosamente con id: {}", id);
    }
}
