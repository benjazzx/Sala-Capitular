package Biblioteca.example.Catalogo.controller;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@RequiredArgsConstructor
@Tag(name = "Catálogos", description = "Operaciones CRUD para la gestión de catálogos")
public class CatalogoController {

    private final CatalogoService service;

    @GetMapping
    @Operation(
            summary = "Listar todos los catálogos",
            description = "Retorna una lista con todos los catálogos registrados en el sistema."
    )
    public List<CatalogoResponseDTO> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar catálogo por ID",
            description = "Retorna un catálogo específico según su identificador."
    )
    public ResponseEntity<CatalogoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Crear un nuevo catálogo",
            description = "Registra un nuevo catálogo en el sistema."
    )
    public ResponseEntity<CatalogoResponseDTO> guardar(@Valid @RequestBody CatalogoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un catálogo",
            description = "Actualiza los datos de un catálogo existente según su ID."
    )
    public ResponseEntity<CatalogoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CatalogoRequestDTO dto
    ) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un catálogo",
            description = "Elimina un catálogo existente según su ID."
    )
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}