package Biblioteca.example.User.service;

import Biblioteca.example.User.client.RolClient;
import Biblioteca.example.User.dto.LoginRequestDTO;
import Biblioteca.example.User.dto.RolResponseDTO;
import Biblioteca.example.User.dto.UserRequestDTO;
import Biblioteca.example.User.dto.UserResponseDTO;
import Biblioteca.example.User.model.User;
import Biblioteca.example.User.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RolClient rolClient;

    private UserResponseDTO mapToDTO(User user) {
        String rolNombre = null;
        try {
            RolResponseDTO rol = rolClient.obtenerPorId(user.getRolId());
            rolNombre = rol.getNombre();
        } catch (FeignException e) {
            rolNombre = "Desconocido";
        }
        return new UserResponseDTO(user.getId(), user.getNombre(), user.getApellido(),
                user.getEmail(), user.getRolId(), rolNombre);
    }

    private void validarRol(Long rolId) {
        try {
            rolClient.obtenerPorId(rolId);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El rol no existe con id: " + rolId);
        } catch (FeignException e) {
            throw new RuntimeException("No se puede contactar con el servicio de roles: " + e.getMessage());
        }
    }

    public List<UserResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    public UserResponseDTO login(LoginRequestDTO dto) {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Credenciales inválidas.");
        }
        return mapToDTO(user);
    }

    public UserResponseDTO guardar(UserRequestDTO dto) {
        validarRol(dto.getRolId());
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        User user = new User(null, dto.getNombre(), dto.getApellido(), dto.getEmail(),
                dto.getPassword(), dto.getRolId());
        return mapToDTO(repository.save(user));
    }

    public UserResponseDTO actualizar(Long id, UserRequestDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        validarRol(dto.getRolId());
        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setRolId(dto.getRolId());
        return mapToDTO(repository.save(user));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
