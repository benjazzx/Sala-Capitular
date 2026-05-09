package Biblioteca.example.Detalle.controller;
import Biblioteca.example.Detalle.dto.DetalleRequestDTO;
import Biblioteca.example.Detalle.dto.DetalleResponseDTO;
import Biblioteca.example.Detalle.service.DetalleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
public class DetalleController {
    private final DetalleService service;
    @GetMapping
    public List<DetalleResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<DetalleResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<DetalleResponseDTO> guardar(@Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<DetalleResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody DetalleRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
