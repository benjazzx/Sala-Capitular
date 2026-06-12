package Biblioteca.example.User.controller;

import Biblioteca.example.User.dto.LoginRequestDTO;
import Biblioteca.example.User.dto.UserRequestDTO;
import Biblioteca.example.User.dto.UserResponseDTO;
import Biblioteca.example.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones para la gestión de usuarios y autenticación")
public class UserController {

    private final UserService service;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista con todos los usuarios registrados.")
    public List<UserResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Retorna un usuario específico según su identificador.")
    public ResponseEntity<UserResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con email y contraseña.")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema.")
    public ResponseEntity<UserResponseDTO> guardar(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente según su ID.")
    public ResponseEntity<UserResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
