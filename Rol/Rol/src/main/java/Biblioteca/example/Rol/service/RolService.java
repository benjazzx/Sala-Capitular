package Biblioteca.example.Rol.service;

import Biblioteca.example.Rol.dto.RolRequestDTO;
import Biblioteca.example.Rol.dto.RolResponseDTO;
import Biblioteca.example.Rol.model.Rol;
import Biblioteca.example.Rol.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository repository;

    private RolResponseDTO mapToDTO(Rol rol) {
        return new RolResponseDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    public List<RolResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los roles");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<RolResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando rol con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public RolResponseDTO guardar(RolRequestDTO dto) {
        log.info("Creando nuevo rol: {}", dto.getNombre());
        Rol rol = new Rol(null, dto.getNombre(), dto.getDescripcion());
        Rol saved = repository.save(rol);
        log.info("Rol creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public RolResponseDTO actualizar(Long id, RolRequestDTO dto) {
        log.info("Actualizando rol con id: {}", id);
        Rol rol = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rol no encontrado con id: {}", id);
                    return new RuntimeException("Rol no encontrado con id: " + id);
                });
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        Rol updated = repository.save(rol);
        log.info("Rol actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando rol con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar rol inexistente con id: {}", id);
            throw new RuntimeException("Rol no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Rol eliminado exitosamente con id: {}", id);
    }
}
