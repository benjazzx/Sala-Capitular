package Biblioteca.example.ReservaLibro.controller;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroRequestDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroResponseDTO;
import Biblioteca.example.ReservaLibro.service.ReservaLibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaLibroController {
    private final ReservaLibroService service;
    @GetMapping
    public List<ReservaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<ReservaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<ReservaLibroResponseDTO> guardar(@Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReservaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
