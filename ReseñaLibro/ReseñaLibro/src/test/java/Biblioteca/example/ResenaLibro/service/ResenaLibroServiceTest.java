package Biblioteca.example.ResenaLibro.service;

import Biblioteca.example.ResenaLibro.client.LibroClient;
import Biblioteca.example.ResenaLibro.client.UserClient;
import Biblioteca.example.ResenaLibro.dto.*;
import Biblioteca.example.ResenaLibro.model.ResenaLibro;
import Biblioteca.example.ResenaLibro.repository.ResenaLibroRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaLibroServiceTest {

    @Mock private ResenaLibroRepository repository;
    @Mock private LibroClient libroClient;
    @Mock private UserClient userClient;

    @InjectMocks private ResenaLibroService service;

    private ResenaLibro resenaEjemplo() {
        return new ResenaLibro(1L, 1L, 1L, 5, "Excelente", LocalDate.of(2024, 3, 15));
    }

    private ResenaLibroRequestDTO dtoValido() {
        return new ResenaLibroRequestDTO(1L, 1L, 5, "Excelente", LocalDate.of(2024, 3, 15));
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(resenaEjemplo()));
        List<ResenaLibroResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCalificacion()).isEqualTo(5);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(resenaEjemplo()));
        Optional<ResenaLibroResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void obtenerPorLibro_debeRetornarResenas() {
        when(repository.findByLibroId(1L)).thenReturn(List.of(resenaEjemplo()));
        List<ResenaLibroResponseDTO> resultado = service.obtenerPorLibro(1L);
        assertThat(resultado).hasSize(1);
    }

    @Test
    void guardar_datosValidos_debeCrear() {
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(ResenaLibro.class))).thenReturn(resenaEjemplo());

        ResenaLibroResponseDTO resultado = service.guardar(dtoValido());

        assertThat(resultado.getCalificacion()).isEqualTo(5);
    }

    @Test
    void guardar_userInexistente_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_libroInexistente_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(resenaEjemplo()));
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(ResenaLibro.class))).thenAnswer(i -> i.getArgument(0));

        ResenaLibroResponseDTO resultado = service.actualizar(1L,
                new ResenaLibroRequestDTO(1L, 1L, 4, "Muy bueno", LocalDate.now()));

        assertThat(resultado.getCalificacion()).isEqualTo(4);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, dtoValido()));
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
    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<ResenaLibroResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }
    @Test
    void guardar_cuandoServicioUserCae_debeLanzarExcepcion() {
        FeignException ex = mock(FeignException.class);
        when(userClient.obtenerPorId(1L)).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_cuandoServicioLibroCae_debeLanzarExcepcion() {
        FeignException ex = mock(FeignException.class);
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }
}