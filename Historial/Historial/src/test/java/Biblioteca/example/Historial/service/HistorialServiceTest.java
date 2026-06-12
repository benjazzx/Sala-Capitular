package Biblioteca.example.Historial.service;

import Biblioteca.example.Historial.client.EstadoClient;
import Biblioteca.example.Historial.client.LibroClient;
import Biblioteca.example.Historial.client.UserClient;
import Biblioteca.example.Historial.dto.*;
import Biblioteca.example.Historial.model.Historial;
import Biblioteca.example.Historial.repository.HistorialRepository;
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
class HistorialServiceTest {

    @Mock private HistorialRepository repository;
    @Mock private LibroClient libroClient;
    @Mock private UserClient userClient;
    @Mock private EstadoClient estadoClient;

    @InjectMocks private HistorialService service;

    private Historial historialEjemplo() {
        return new Historial(1L, 1L, 1L, LocalDate.of(2024, 1, 10), null, 1L);
    }

    private HistorialRequestDTO dtoValido() {
        return new HistorialRequestDTO(1L, 1L, LocalDate.of(2024, 1, 10), null, 1L);
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(historialEjemplo()));
        List<HistorialResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(historialEjemplo()));
        Optional<HistorialResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void guardar_datosValidos_debeCrear() {
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(estadoClient.obtenerPorId(1L)).thenReturn(new EstadoResponseDTO());
        when(repository.save(any(Historial.class))).thenReturn(historialEjemplo());

        HistorialResponseDTO resultado = service.guardar(dtoValido());

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void guardar_libroInexistente_debeLanzarExcepcion() {
        when(libroClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_userInexistente_debeLanzarExcepcion() {
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(userClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(historialEjemplo()));
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(estadoClient.obtenerPorId(1L)).thenReturn(new EstadoResponseDTO());
        when(repository.save(any(Historial.class))).thenAnswer(i -> i.getArgument(0));

        HistorialResponseDTO resultado = service.actualizar(1L, dtoValido());

        assertThat(resultado).isNotNull();
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
}
