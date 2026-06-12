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
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones para la gestión de reservas de libros")
public class ReservaLibroController {

    private final ReservaLibroService service;

    @GetMapping
    @Operation(summary = "Listar todas las reservas", description = "Retorna una lista con todas las reservas registradas.")
    public List<ReservaLibroResponseDTO> obtenerTodos() { return service.obtenerTodos(); }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Retorna una reserva específica según su identificador.")
    public ResponseEntity<ReservaLibroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Registra una nueva reserva. Incluye aviso de multas en la respuesta si existen.")
    public ResponseEntity<ReservaLibroResponseDTO> guardar(@Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva", description = "Actualiza los datos de una reserva existente según su ID.")
    public ResponseEntity<ReservaLibroResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaLibroRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Elimina una reserva existente según su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
