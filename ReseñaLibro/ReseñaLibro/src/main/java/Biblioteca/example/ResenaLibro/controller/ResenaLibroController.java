package Biblioteca.example.ResenaLibro.controller;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.service.ResenaLibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@Tag(name = "Resenas", description = "Operaciones CRUD y consulta de resenas")
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaLibroController {
    private final ResenaLibroService service;
    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<ResenaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<ResenaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Listar resenas por libro")
    @GetMapping("/libro/{libroId}")
    public List<ResenaLibroResponseDTO> obtenerPorLibro(@PathVariable Long libroId) {
        return service.obtenerPorLibro(libroId);
    }
    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<ResenaLibroResponseDTO> guardar(@Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<ResenaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
