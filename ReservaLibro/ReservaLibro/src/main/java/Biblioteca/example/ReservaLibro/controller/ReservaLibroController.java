package Biblioteca.example.ReservaLibro.controller;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroRequestDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroResponseDTO;
import Biblioteca.example.ReservaLibro.service.ReservaLibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@Tag(name = "Reservas", description = "Operaciones CRUD para reservas de libros")
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaLibroController {
    private final ReservaLibroService service;
    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<ReservaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<ReservaLibroResponseDTO> guardar(@Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
