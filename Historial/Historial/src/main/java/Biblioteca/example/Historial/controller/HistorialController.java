package Biblioteca.example.Historial.controller;

import Biblioteca.example.Historial.dto.HistorialRequestDTO;
import Biblioteca.example.Historial.dto.HistorialResponseDTO;
import Biblioteca.example.Historial.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historiales")
@RequiredArgsConstructor
@Tag(name = "Historial", description = "Operaciones para la gestión del historial de préstamos")
public class HistorialController {

    private final HistorialService service;

    @GetMapping
    @Operation(summary = "Listar todos los historiales", description = "Retorna una lista con todos los historiales de préstamos.")
    public List<HistorialResponseDTO> obtenerTodos() { return service.obtenerTodos(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar historial por ID", description = "Retorna un historial específico según su identificador.")
    public ResponseEntity<HistorialResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo historial", description = "Registra un nuevo préstamo en el historial.")
    public ResponseEntity<HistorialResponseDTO> guardar(@Valid @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un historial", description = "Actualiza los datos de un historial existente según su ID.")
    public ResponseEntity<HistorialResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un historial", description = "Elimina un historial existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
