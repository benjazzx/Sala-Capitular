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
@Tag(name = "Detalles", description = "Operaciones CRUD para detalles")
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
public class DetalleController {
    private final DetalleService service;
    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<DetalleResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<DetalleResponseDTO> guardar(@Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<DetalleResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
