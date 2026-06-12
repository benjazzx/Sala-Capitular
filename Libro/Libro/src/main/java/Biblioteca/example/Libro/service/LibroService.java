package Biblioteca.example.Libro.service;

import Biblioteca.example.Libro.client.CatalogoClient;
import Biblioteca.example.Libro.client.EstadoClient;
import Biblioteca.example.Libro.client.UserClient;
import Biblioteca.example.Libro.dto.LibroRequestDTO;
import Biblioteca.example.Libro.dto.LibroResponseDTO;
import Biblioteca.example.Libro.dto.UserResponseDTO;
import Biblioteca.example.Libro.model.Libro;
import Biblioteca.example.Libro.repository.LibroRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository repository;
    private final CatalogoClient catalogoClient;
    private final EstadoClient estadoClient;
    private final UserClient userClient;

    private LibroResponseDTO mapToDTO(Libro libro) {
        return new LibroResponseDTO(libro.getId(), libro.getTitulo(), libro.getIsbn(),
                libro.getAnio(), libro.getDescripcion(), libro.getAutorId(),
                libro.getCatalogoId(), libro.getEstadoId());
    }

    private void validarCatalogo(Long catalogoId) {
        try {
            catalogoClient.obtenerPorId(catalogoId);
        } catch (FeignException.NotFound ex) {
            log.warn("Catálogo no encontrado con id: {}", catalogoId);
            throw new RuntimeException("El catálogo no existe con id: " + catalogoId);
        } catch (FeignException e) {
            log.error("Error al contactar servicio de catálogos: {}", e.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de catálogos: " + e.getMessage());
        }
    }

    private void validarEstado(Long estadoId) {
        try {
            estadoClient.obtenerPorId(estadoId);
        } catch (FeignException.NotFound ex) {
            log.warn("Estado no encontrado con id: {}", estadoId);
            throw new RuntimeException("El estado no existe con id: " + estadoId);
        } catch (FeignException e) {
            log.error("Error al contactar servicio de estados: {}", e.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de estados: " + e.getMessage());
        }
    }

    private void validarAutor(Long autorId) {
        try {
            UserResponseDTO user = userClient.obtenerPorId(autorId);
            if (!"AUTOR".equals(user.getRolNombre())) {
                log.warn("El usuario con id {} no tiene el rol de AUTOR", autorId);
                throw new RuntimeException("El usuario con id " + autorId + " no tiene el rol de AUTOR");
            }
        } catch (FeignException.NotFound ex) {
            log.warn("Autor no encontrado con id: {}", autorId);
            throw new RuntimeException("El autor no existe con id: " + autorId);
        } catch (FeignException e) {
            log.error("Error al contactar servicio de usuarios: {}", e.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de usuarios: " + e.getMessage());
        }
    }

    public List<LibroResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los libros");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<LibroResponseDTO> obtenerPorAutor(Long autorId) {
        log.info("Obteniendo libros del autor con id: {}", autorId);
        List<LibroResponseDTO> libros = repository.findByAutorId(autorId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
        log.info("Se encontraron {} libros para el autor con id: {}", libros.size(), autorId);
        return libros;
    }

    public Optional<LibroResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando libro con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public LibroResponseDTO guardar(LibroRequestDTO dto) {
        log.info("Creando nuevo libro: {}", dto.getTitulo());
        validarAutor(dto.getAutorId());
        validarCatalogo(dto.getCatalogoId());
        validarEstado(dto.getEstadoId());
        if (dto.getIsbn() != null && repository.existsByIsbn(dto.getIsbn())) {
            log.warn("Validación fallida: ISBN duplicado {}", dto.getIsbn());
            throw new RuntimeException("Ya existe un libro con el ISBN: " + dto.getIsbn());
        }
        Libro libro = new Libro(null, dto.getTitulo(), dto.getIsbn(), dto.getAnio(),
                dto.getDescripcion(), dto.getAutorId(), dto.getCatalogoId(), dto.getEstadoId());
        Libro saved = repository.save(libro);
        log.info("Libro creado exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public LibroResponseDTO actualizar(Long id, LibroRequestDTO dto) {
        log.info("Actualizando libro con id: {}", id);
        Libro libro = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Libro no encontrado con id: {}", id);
                    return new RuntimeException("Libro no encontrado con id: " + id);
                });
        validarAutor(dto.getAutorId());
        validarCatalogo(dto.getCatalogoId());
        validarEstado(dto.getEstadoId());
        libro.setTitulo(dto.getTitulo());
        libro.setIsbn(dto.getIsbn());
        libro.setAnio(dto.getAnio());
        libro.setDescripcion(dto.getDescripcion());
        libro.setAutorId(dto.getAutorId());
        libro.setCatalogoId(dto.getCatalogoId());
        libro.setEstadoId(dto.getEstadoId());
        Libro updated = repository.save(libro);
        log.info("Libro actualizado exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando libro con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar libro inexistente con id: {}", id);
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Libro eliminado exitosamente con id: {}", id);
    }
}
