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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            throw new RuntimeException("El catálogo no existe con id: " + catalogoId);
        } catch (FeignException e) {
            throw new RuntimeException("No se puede contactar con el servicio de catálogos: " + e.getMessage());
        }
    }

    private void validarEstado(Long estadoId) {
        try {
            estadoClient.obtenerPorId(estadoId);
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El estado no existe con id: " + estadoId);
        } catch (FeignException e) {
            throw new RuntimeException("No se puede contactar con el servicio de estados: " + e.getMessage());
        }
    }

    private void validarAutor(Long autorId) {
        try {
            UserResponseDTO user = userClient.obtenerPorId(autorId);
            if (!"AUTOR".equals(user.getRolNombre())) {
                throw new RuntimeException("El usuario con id " + autorId + " no tiene el rol de AUTOR");
            }
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El autor no existe con id: " + autorId);
        } catch (FeignException e) {
            throw new RuntimeException("No se puede contactar con el servicio de usuarios: " + e.getMessage());
        }
    }

    public List<LibroResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<LibroResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }

    public LibroResponseDTO guardar(LibroRequestDTO dto) {
        validarAutor(dto.getAutorId());
        validarCatalogo(dto.getCatalogoId());
        validarEstado(dto.getEstadoId());
        if (dto.getIsbn() != null && repository.existsByIsbn(dto.getIsbn())) {
            throw new RuntimeException("Ya existe un libro con el ISBN: " + dto.getIsbn());
        }
        Libro libro = new Libro(null, dto.getTitulo(), dto.getIsbn(), dto.getAnio(),
                dto.getDescripcion(), dto.getAutorId(), dto.getCatalogoId(), dto.getEstadoId());
        return mapToDTO(repository.save(libro));
    }

    public LibroResponseDTO actualizar(Long id, LibroRequestDTO dto) {
        Libro libro = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
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
        return mapToDTO(repository.save(libro));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}
