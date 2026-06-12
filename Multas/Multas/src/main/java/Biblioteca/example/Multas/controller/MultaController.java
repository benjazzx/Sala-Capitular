package Biblioteca.example.Multas.controller;
import Biblioteca.example.Multas.dto.EstadoMultasDTO;
import Biblioteca.example.Multas.dto.MultaRequestDTO;
import Biblioteca.example.Multas.dto.MultaResponseDTO;
import Biblioteca.example.Multas.service.MultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@Tag(name = "Multas", description = "Operaciones de multas y estado de usuarios")
@RequestMapping("/api/multas")
@RequiredArgsConstructor
public class MultaController {
    private final MultaService service;
    @Operation(summary = "Listar recursos")
    @GetMapping
    public List<MultaResponseDTO> obtenerTodos() { return service.obtenerTodos(); }
    @Operation(summary = "Obtener recurso por id")
    @GetMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Listar multas por usuario")
    @GetMapping("/usuario/{userId}")
    public List<MultaResponseDTO> obtenerPorUsuario(@PathVariable Long userId) {
        return service.obtenerPorUsuario(userId);
    }
    @Operation(summary = "Verificar si el usuario puede reservar")
    @GetMapping("/usuario/{userId}/puede-pedir")
    public ResponseEntity<Boolean> puedeReservar(@PathVariable Long userId) {
        return ResponseEntity.ok(service.puedeReservar(userId));
    }
    @Operation(summary = "Obtener estado de multas del usuario")
    @GetMapping("/usuario/{userId}/estado")
    public ResponseEntity<EstadoMultasDTO> obtenerEstadoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(service.obtenerEstadoUsuario(userId));
    }
    @Operation(summary = "Listar multas por no entrega")
    @GetMapping("/no-entrega")
    public List<MultaResponseDTO> obtenerNoEntrega() {
        return service.obtenerPorTipo("NO_ENTREGA");
    }
    @Operation(summary = "Listar multas por tipo")
    @GetMapping("/tipo/{tipo}")
    public List<MultaResponseDTO> obtenerPorTipo(@PathVariable String tipo) {
        return service.obtenerPorTipo(tipo);
    }
    @Operation(summary = "Crear recurso")
    @PostMapping
    public ResponseEntity<MultaResponseDTO> guardar(@Valid @RequestBody MultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }
    @Operation(summary = "Actualizar recurso")
    @PutMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody MultaRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
    @Operation(summary = "Eliminar recurso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
