package Biblioteca.example.Libro.service;

import Biblioteca.example.Libro.client.CatalogoClient;
import Biblioteca.example.Libro.client.EstadoClient;
import Biblioteca.example.Libro.client.UserClient;
import Biblioteca.example.Libro.dto.*;
import Biblioteca.example.Libro.model.Libro;
import Biblioteca.example.Libro.repository.LibroRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock private LibroRepository repository;
    @Mock private CatalogoClient catalogoClient;
    @Mock private EstadoClient estadoClient;
    @Mock private UserClient userClient;

    @InjectMocks private LibroService service;

    private Libro libroEjemplo() {
        return new Libro(1L, "El Quijote", "ISBN001", 1605, "desc", 2L, 1L, 1L);
    }

    private UserResponseDTO autorEjemplo() {
        return new UserResponseDTO(2L, "Miguel", "Cervantes", "miguel@mail.com", 3L, "AUTOR");
    }

    private CatalogoResponseDTO catalogoEjemplo() {
        return new CatalogoResponseDTO(1L, "Novela", "desc");
    }

    private EstadoResponseDTO estadoEjemplo() {
        return new EstadoResponseDTO(1L, "DISPONIBLE", "desc");
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(libroEjemplo()));
        List<LibroResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTitulo()).isEqualTo("El Quijote");
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(libroEjemplo()));
        Optional<LibroResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void obtenerPorAutor_debeRetornarLibros() {
        when(repository.findByAutorId(2L)).thenReturn(List.of(libroEjemplo()));
        List<LibroResponseDTO> resultado = service.obtenerPorAutor(2L);
        assertThat(resultado).hasSize(1);
    }

    @Test
    void guardar_datosValidos_debeCrear() {
        LibroRequestDTO dto = new LibroRequestDTO("El Quijote", "ISBN001", 1605, "desc", 2L, 1L, 1L);
        when(userClient.obtenerPorId(2L)).thenReturn(autorEjemplo());
        when(catalogoClient.obtenerPorId(1L)).thenReturn(catalogoEjemplo());
        when(estadoClient.obtenerPorId(1L)).thenReturn(estadoEjemplo());
        when(repository.existsByIsbn("ISBN001")).thenReturn(false);
        when(repository.save(any(Libro.class))).thenReturn(libroEjemplo());

        LibroResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getTitulo()).isEqualTo("El Quijote");
    }

    @Test
    void guardar_isbnDuplicado_debeLanzarExcepcion() {
        LibroRequestDTO dto = new LibroRequestDTO("El Quijote", "ISBN001", 1605, "desc", 2L, 1L, 1L);
        when(userClient.obtenerPorId(2L)).thenReturn(autorEjemplo());
        when(catalogoClient.obtenerPorId(1L)).thenReturn(catalogoEjemplo());
        when(estadoClient.obtenerPorId(1L)).thenReturn(estadoEjemplo());
        when(repository.existsByIsbn("ISBN001")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void guardar_autorSinRolAutor_debeLanzarExcepcion() {
        LibroRequestDTO dto = new LibroRequestDTO("Libro", null, 2020, "desc", 1L, 1L, 1L);
        UserResponseDTO userNoAutor = new UserResponseDTO(1L, "Juan", "Perez", "j@mail.com", 2L, "CLIENTE");
        when(userClient.obtenerPorId(1L)).thenReturn(userNoAutor);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void guardar_catalogoInexistente_debeLanzarExcepcion() {
        LibroRequestDTO dto = new LibroRequestDTO("Libro", null, 2020, "desc", 2L, 99L, 1L);
        when(userClient.obtenerPorId(2L)).thenReturn(autorEjemplo());
        when(catalogoClient.obtenerPorId(99L)).thenThrow(FeignException.NotFound.class);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void eliminar_cuandoExiste_debeEliminar() {
        when(repository.existsById(1L)).thenReturn(true);
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.eliminar(99L));
    }
}
