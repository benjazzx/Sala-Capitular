package Biblioteca.example.Estante.controller;
import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.service.EstanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@Tag(name = "Estantes", description = "Operaciones CRUD para estantes")
@RequestMapping("/api/estantes")
@RequiredArgsConstructor
public class EstanteController {
    private final EstanteService service;
    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<EstanteResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<EstanteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<EstanteResponseDTO> guardar(@Valid @RequestBody EstanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<EstanteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstanteRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
