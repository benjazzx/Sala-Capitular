package Biblioteca.example.Multas.controller;
import Biblioteca.example.Multas.dto.EstadoMultasDTO;
import Biblioteca.example.Multas.dto.MultaRequestDTO;
import Biblioteca.example.Multas.dto.MultaResponseDTO;
import Biblioteca.example.Multas.service.MultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/multas")
@RequiredArgsConstructor
public class MultaController {
    private final MultaService service;
    @GetMapping
    public List<MultaResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @GetMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/usuario/{userId}")
    public List<MultaResponseDTO> obtenerPorUsuario(@PathVariable Long userId) {
        return service.obtenerPorUsuario(userId);
    }
    @GetMapping("/usuario/{userId}/puede-pedir")
    public ResponseEntity<Boolean> puedeReservar(@PathVariable Long userId) {
        return ResponseEntity.ok(service.puedeReservar(userId));
    }
    @GetMapping("/usuario/{userId}/estado")
    public ResponseEntity<EstadoMultasDTO> obtenerEstadoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(service.obtenerEstadoUsuario(userId));
    }
    @GetMapping("/no-entrega")
    public List<MultaResponseDTO> obtenerNoEntrega() {
        return service.obtenerPorTipo("NO_ENTREGA");
    }
    @GetMapping("/tipo/{tipo}")
    public List<MultaResponseDTO> obtenerPorTipo(@PathVariable String tipo) {
        return service.obtenerPorTipo(tipo);
    }
    @PostMapping
    public ResponseEntity<MultaResponseDTO> guardar(@Valid @RequestBody MultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MultaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
