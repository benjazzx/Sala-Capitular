package Biblioteca.example.Estado.controller;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.service.EstadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService service;

    @GetMapping
    public List<EstadoResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstadoResponseDTO> guardar(@Valid @RequestBody EstadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EstadoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
