package Biblioteca.example.Estado.controller;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
@Tag(name = "Estados", description = "Operaciones CRUD para la gestión de estados")
public class EstadoController {

    private final EstadoService service;

    @GetMapping
    @Operation(summary = "Listar todos los estados", description = "Retorna una lista con todos los estados registrados en el sistema.")
    public List<EstadoResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado por ID", description = "Retorna un estado específico según su identificador.")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo estado", description = "Registra un nuevo estado en el sistema.")
    public ResponseEntity<EstadoResponseDTO> guardar(@Valid @RequestBody EstadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un estado", description = "Actualiza los datos de un estado existente según su ID.")
    public ResponseEntity<EstadoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un estado", description = "Elimina un estado existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
