package Biblioteca.example.ResenaLibro.controller;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.service.ResenaLibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaLibroController {
    private final ResenaLibroService service;
    @GetMapping
    public List<ResenaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<ResenaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/libro/{libroId}")
    public List<ResenaLibroResponseDTO> obtenerPorLibro(@PathVariable Long libroId) {
        return service.obtenerPorLibro(libroId);
    }
    @PostMapping
    public ResponseEntity<ResenaLibroResponseDTO> guardar(@Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResenaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
