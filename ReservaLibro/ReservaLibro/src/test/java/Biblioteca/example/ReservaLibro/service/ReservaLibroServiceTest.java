package Biblioteca.example.ReservaLibro.service;

import Biblioteca.example.ReservaLibro.client.LibroClient;
import Biblioteca.example.ReservaLibro.client.MultasClient;
import Biblioteca.example.ReservaLibro.client.UserClient;
import Biblioteca.example.ReservaLibro.dto.*;
import Biblioteca.example.ReservaLibro.model.ReservaLibro;
import Biblioteca.example.ReservaLibro.repository.ReservaLibroRepository;
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
class ReservaLibroServiceTest {

    @Mock private ReservaLibroRepository repository;
    @Mock private LibroClient libroClient;
    @Mock private UserClient userClient;
    @Mock private MultasClient multasClient;

    @InjectMocks private ReservaLibroService service;

    private ReservaLibro reservaEjemplo() {
        return new ReservaLibro(1L, 1L, 1L, LocalDate.of(2024, 6, 1), "ACTIVA");
    }

    private ReservaLibroRequestDTO dtoValido() {
        return new ReservaLibroRequestDTO(1L, 1L, LocalDate.of(2024, 6, 1), "ACTIVA");
    }

    private EstadoMultasDTO sinMultas() {
        return new EstadoMultasDTO(true, 0, 0, "Sin multas pendientes.");
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(reservaEjemplo()));
        List<ReservaLibroResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(reservaEjemplo()));
        Optional<ReservaLibroResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void guardar_sinMultas_debeCrear() {
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(multasClient.obtenerEstado(1L)).thenReturn(sinMultas());
        when(repository.findByLibroIdAndEstadoReserva(1L, "ACTIVA")).thenReturn(Optional.empty());
        when(repository.save(any(ReservaLibro.class))).thenReturn(reservaEjemplo());

        ReservaLibroResponseDTO resultado = service.guardar(dtoValido());

        assertThat(resultado.getEstadoReserva()).isEqualTo("ACTIVA");
    }

    @Test
    void guardar_usuarioBloqueadoPorMultas_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        EstadoMultasDTO bloqueado = new EstadoMultasDTO(false, 2, 5, "RESERVA BLOQUEADA.");
        when(multasClient.obtenerEstado(1L)).thenReturn(bloqueado);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_libroConReservaActiva_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(multasClient.obtenerEstado(1L)).thenReturn(sinMultas());
        when(repository.findByLibroIdAndEstadoReserva(1L, "ACTIVA"))
                .thenReturn(Optional.of(reservaEjemplo()));

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(reservaEjemplo()));
        when(userClient.obtenerPorId(1L)).thenReturn(new UserResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(ReservaLibro.class))).thenAnswer(i -> i.getArgument(0));

        ReservaLibroResponseDTO resultado = service.actualizar(1L,
                new ReservaLibroRequestDTO(1L, 1L, LocalDate.now(), "COMPLETADA"));

        assertThat(resultado).isNotNull();
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
