package Biblioteca.example.Estado.service;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.model.Estado;
import Biblioteca.example.Estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository repository;

    private EstadoResponseDTO mapToDTO(Estado estado) {
        return new EstadoResponseDTO(estado.getId(), estado.getNombre(), estado.getDescripcion());
    }

    public List<EstadoResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los estados");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<EstadoResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando estado con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public EstadoResponseDTO guardar(EstadoRequestDTO dto) {
        log.info("Creando nuevo estado: {}", dto.getNombre());
        Estado estado = new Estado(null, dto.getNombre(), dto.getDescripcion());
        Estado saved = repository.save(estado);
        log.info("Estado creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public EstadoResponseDTO actualizar(Long id, EstadoRequestDTO dto) {
        log.info("Actualizando estado con id: {}", id);
        Estado estado = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Estado no encontrado con id: {}", id);
                    return new RuntimeException("Estado no encontrado con id: " + id);
                });
        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        Estado updated = repository.save(estado);
        log.info("Estado actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando estado con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar estado inexistente con id: {}", id);
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Estado eliminado exitosamente con id: {}", id);
    }
}
