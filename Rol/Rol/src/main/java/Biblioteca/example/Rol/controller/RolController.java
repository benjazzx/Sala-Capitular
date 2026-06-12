package Biblioteca.example.Rol.controller;

import Biblioteca.example.Rol.dto.RolRequestDTO;
import Biblioteca.example.Rol.dto.RolResponseDTO;
import Biblioteca.example.Rol.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operaciones CRUD para la gestión de roles")
public class RolController {

    private final RolService service;

    @GetMapping
    @Operation(summary = "Listar todos los roles", description = "Retorna una lista con todos los roles registrados en el sistema.")
    public List<RolResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar rol por ID", description = "Retorna un rol específico según su identificador.")
    public ResponseEntity<RolResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo rol", description = "Registra un nuevo rol en el sistema.")
    public ResponseEntity<RolResponseDTO> guardar(@Valid @RequestBody RolRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rol", description = "Actualiza los datos de un rol existente según su ID.")
    public ResponseEntity<RolResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody RolRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rol", description = "Elimina un rol existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
