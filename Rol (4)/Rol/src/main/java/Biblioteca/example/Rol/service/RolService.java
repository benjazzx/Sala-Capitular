package Biblioteca.example.Rol.service;

import Biblioteca.example.Rol.dto.RolRequestDTO;
import Biblioteca.example.Rol.dto.RolResponseDTO;
import Biblioteca.example.Rol.model.Rol;
import Biblioteca.example.Rol.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository repository;

    private RolResponseDTO mapToDTO(Rol rol) {
        return new RolResponseDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    public List<RolResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<RolResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    public RolResponseDTO guardar(RolRequestDTO dto) {
        Rol rol = new Rol(null, dto.getNombre(), dto.getDescripcion());
        return mapToDTO(repository.save(rol));
    }

    public RolResponseDTO actualizar(Long id, RolRequestDTO dto) {
        Rol rol = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con id: " + id));
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        return mapToDTO(repository.save(rol));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
