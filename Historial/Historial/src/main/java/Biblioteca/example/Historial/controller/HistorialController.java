package Biblioteca.example.Historial.controller;
import Biblioteca.example.Historial.dto.HistorialRequestDTO;
import Biblioteca.example.Historial.dto.HistorialResponseDTO;
import Biblioteca.example.Historial.service.HistorialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/historiales")
@RequiredArgsConstructor
public class HistorialController {
    private final HistorialService service;
    @GetMapping
    public List<HistorialResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<HistorialResponseDTO> guardar(@Valid @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<HistorialResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
