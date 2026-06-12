package Biblioteca.example.Detalle.controller;

import Biblioteca.example.Detalle.dto.DetalleRequestDTO;
import Biblioteca.example.Detalle.dto.DetalleResponseDTO;
import Biblioteca.example.Detalle.service.DetalleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
@Tag(name = "Detalles", description = "Operaciones para la gestión de detalles de préstamos")
public class DetalleController {

    private final DetalleService service;

    @GetMapping
    @Operation(summary = "Listar todos los detalles", description = "Retorna una lista con todos los detalles registrados.")
    public List<DetalleResponseDTO> obtenerTodos() { return service.obtenerTodos(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle por ID", description = "Retorna un detalle específico según su identificador.")
    public ResponseEntity<DetalleResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo detalle", description = "Registra un nuevo detalle de préstamo.")
    public ResponseEntity<DetalleResponseDTO> guardar(@Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un detalle", description = "Actualiza los datos de un detalle existente según su ID.")
    public ResponseEntity<DetalleResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un detalle", description = "Elimina un detalle existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
