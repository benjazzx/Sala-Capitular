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
@RequestMapping("/api/multas")
@RequiredArgsConstructor
@Tag(name = "Multas", description = "Operaciones para la gestión de multas y sanciones de usuarios")
public class MultaController {

    private final MultaService service;

    @GetMapping
    @Operation(
            summary = "Listar todas las multas",
            description = "Retorna una lista con todas las multas registradas en el sistema."
    )
    public List<MultaResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar multa por ID",
            description = "Retorna una multa específica según su identificador."
    )
    public ResponseEntity<MultaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{userId}")
    @Operation(
            summary = "Listar multas por usuario",
            description = "Retorna todas las multas asociadas a un usuario específico."
    )
    public List<MultaResponseDTO> obtenerPorUsuario(@PathVariable Long userId) {
        return service.obtenerPorUsuario(userId);
    }

    @GetMapping("/usuario/{userId}/puede-pedir")
    @Operation(
            summary = "Consultar si el usuario puede pedir libros",
            description = "Retorna verdadero o falso según el estado de multas acumuladas del usuario."
    )
    public ResponseEntity<Boolean> puedeReservar(@PathVariable Long userId) {
        return ResponseEntity.ok(service.puedeReservar(userId));
    }

    @GetMapping("/usuario/{userId}/estado")
    @Operation(
            summary = "Obtener estado de multas del usuario",
            description = "Retorna el estado general de multas del usuario, incluyendo si puede reservar, total de multas, cantidad acumulada y aviso."
    )
    public ResponseEntity<EstadoMultasDTO> obtenerEstadoUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(service.obtenerEstadoUsuario(userId));
    }

    @GetMapping("/no-entrega")
    @Operation(
            summary = "Listar multas por no entrega",
            description = "Retorna todas las multas clasificadas con el tipo NO_ENTREGA."
    )
    public List<MultaResponseDTO> obtenerNoEntrega() {
        return service.obtenerPorTipo("NO_ENTREGA");
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Listar multas por tipo",
            description = "Retorna las multas filtradas por tipo. Los valores permitidos son NO_ENTREGA, DAÑO o RETRASO."
    )
    public List<MultaResponseDTO> obtenerPorTipo(@PathVariable String tipo) {
        return service.obtenerPorTipo(tipo);
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva multa",
            description = "Registra una nueva multa asociada a un usuario, un administrador y un historial de préstamo."
    )
    public ResponseEntity<MultaResponseDTO> guardar(@Valid @RequestBody MultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una multa",
            description = "Actualiza los datos de una multa existente según su ID."
    )
    public ResponseEntity<MultaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MultaRequestDTO dto
    ) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una multa",
            description = "Elimina una multa existente según su ID."
    )
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}