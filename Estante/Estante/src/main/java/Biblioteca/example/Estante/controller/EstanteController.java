package Biblioteca.example.Estante.controller;
import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.service.EstanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/estantes")
@RequiredArgsConstructor
public class EstanteController {
    private final EstanteService service;
    @GetMapping
    public List<EstanteResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<EstanteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<EstanteResponseDTO> guardar(@Valid @RequestBody EstanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<EstanteResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstanteRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
