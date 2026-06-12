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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RolClient rolClient;
    private final PasswordEncoder passwordEncoder;

    private UserResponseDTO mapToDTO(User user) {
        String rolNombre = null;
        try {
            RolResponseDTO rol = rolClient.obtenerPorId(user.getRolId());
            rolNombre = rol.getNombre();
        } catch (FeignException e) {
            log.error("Error al obtener rol con id {}: {}", user.getRolId(), e.getMessage());
            rolNombre = "Desconocido";
        }
        return new UserResponseDTO(user.getId(), user.getNombre(), user.getApellido(),
                user.getEmail(), user.getRolId(), rolNombre);
    }

    private RolResponseDTO validarYObtenerRol(Long rolId) {
        try {
            return rolClient.obtenerPorId(rolId);
        } catch (FeignException.NotFound ex) {
            log.warn("Rol no encontrado con id: {}", rolId);
            throw new RuntimeException("El rol no existe con id: " + rolId);
        } catch (FeignException e) {
            log.error("Error al contactar servicio de roles: {}", e.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de roles: " + e.getMessage());
        }
    }

    public List<UserResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public UserResponseDTO login(LoginRequestDTO dto) {
        log.info("Intento de login para email: {}", dto.getEmail());
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login fallido: email no encontrado {}", dto.getEmail());
                    return new RuntimeException("Credenciales inválidas.");
                });
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Login fallido: contraseña incorrecta para {}", dto.getEmail());
            throw new RuntimeException("Credenciales inválidas.");
        }
        log.info("Login exitoso para usuario id: {} con rol id: {}", user.getId(), user.getRolId());
        return mapToDTO(user);
    }

    public UserResponseDTO guardar(UserRequestDTO dto) {
        log.info("Creando nuevo usuario: {} {}", dto.getNombre(), dto.getApellido());
        RolResponseDTO rol = validarYObtenerRol(dto.getRolId());
        if ("ADMIN".equalsIgnoreCase(rol.getNombre())) {
            log.warn("Intento de registro con rol ADMIN bloqueado para email: {}", dto.getEmail());
            throw new RuntimeException("No se puede registrar un usuario con rol ADMIN.");
        }
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Validación fallida: email duplicado {}", dto.getEmail());
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }
        User user = new User(null, dto.getNombre(), dto.getApellido(), dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()), dto.getRolId());
        User saved = repository.save(user);
        log.info("Usuario creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public UserResponseDTO actualizar(Long id, UserRequestDTO dto) {
        log.info("Actualizando usuario con id: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new RuntimeException("Usuario no encontrado con id: " + id);
                });
        validarYObtenerRol(dto.getRolId());
        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setRolId(dto.getRolId());
        User updated = repository.save(user);
        log.info("Usuario actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar usuario inexistente con id: {}", id);
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Usuario eliminado exitosamente con id: {}", id);
    }
}
