package Biblioteca.example.Estado.service;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.model.Estado;
import Biblioteca.example.Estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository repository;

    private EstadoResponseDTO mapToDTO(Estado estado) {
        return new EstadoResponseDTO(estado.getId(), estado.getNombre(), estado.getDescripcion());
    }

    public List<EstadoResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<EstadoResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    public EstadoResponseDTO guardar(EstadoRequestDTO dto) {
        Estado estado = new Estado(null, dto.getNombre(), dto.getDescripcion());
        return mapToDTO(repository.save(estado));
    }

    public EstadoResponseDTO actualizar(Long id, EstadoRequestDTO dto) {
        Estado estado = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
        estado.setNombre(dto.getNombre());
        estado.setDescripcion(dto.getDescripcion());
        return mapToDTO(repository.save(estado));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
