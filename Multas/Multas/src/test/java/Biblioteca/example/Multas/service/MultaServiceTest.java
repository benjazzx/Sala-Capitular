package Biblioteca.example.Multas.service;

import Biblioteca.example.Multas.client.HistorialClient;
import Biblioteca.example.Multas.client.UserClient;
import Biblioteca.example.Multas.dto.*;
import Biblioteca.example.Multas.model.Multa;
import Biblioteca.example.Multas.repository.MultaRepository;
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
class MultaServiceTest {

    @Mock private MultaRepository repository;
    @Mock private UserClient userClient;
    @Mock private HistorialClient historialClient;

    @InjectMocks private MultaService service;

    private UserResponseDTO adminEjemplo() {
        return new UserResponseDTO(1L, "Admin", "User", "admin@mail.com", 1L, "ADMIN");
    }

    private UserResponseDTO clienteEjemplo() {
        return new UserResponseDTO(2L, "Juan", "Perez", "juan@mail.com", 2L, "CLIENTE");
    }

    private Multa multaEjemplo() {
        return new Multa(1L, 2L, 1L, "desc", LocalDate.of(2024, 1, 1), 1, 1L, "RETRASO");
    }

    private MultaRequestDTO dtoValido() {
        return new MultaRequestDTO(1L, 2L, 1L, "retraso en devolución",
                LocalDate.of(2024, 1, 1), 1, "RETRASO");
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(multaEjemplo()));
        List<MultaResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(multaEjemplo()));
        Optional<MultaResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
    }

    @Test
    void obtenerPorUsuario_debeRetornarMultas() {
        when(repository.findByUserId(2L)).thenReturn(List.of(multaEjemplo()));
        List<MultaResponseDTO> resultado = service.obtenerPorUsuario(2L);
        assertThat(resultado).hasSize(1);
    }

    @Test
    void puedeReservar_totalMenorIgual3_debeRetornarTrue() {
        when(repository.sumCantidadByUserId(2L)).thenReturn(2);
        assertThat(service.puedeReservar(2L)).isTrue();
    }

    @Test
    void puedeReservar_totalMayor3_debeRetornarFalse() {
        when(repository.sumCantidadByUserId(2L)).thenReturn(5);
        assertThat(service.puedeReservar(2L)).isFalse();
    }

    @Test
    void guardar_datosValidos_debeCrear() {
        when(userClient.obtenerPorId(1L)).thenReturn(adminEjemplo());
        when(userClient.obtenerPorId(2L)).thenReturn(clienteEjemplo());
        when(historialClient.obtenerPorId(1L)).thenReturn(new HistorialResponseDTO());
        when(repository.save(any(Multa.class))).thenReturn(multaEjemplo());

        MultaResponseDTO resultado = service.guardar(dtoValido());

        assertThat(resultado.getTipo()).isEqualTo("RETRASO");
    }

    @Test
    void guardar_adminSinRolAdmin_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(clienteEjemplo()); // rol CLIENTE, no ADMIN
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_adminInexistente_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
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
    void obtenerEstadoUsuario_sinMultas_debeRetornarSinPendientes() {
        when(repository.findByUserId(2L)).thenReturn(List.of());
        when(repository.sumCantidadByUserId(2L)).thenReturn(0);
        EstadoMultasDTO estado = service.obtenerEstadoUsuario(2L);
        assertThat(estado.isPuedeReservar()).isTrue();
        assertThat(estado.getTotalMultas()).isEqualTo(0);
    }
    @Test
    void obtenerPorTipo_debeRetornarMultas() {
        when(repository.findByTipo("RETRASO")).thenReturn(List.of(multaEjemplo()));

        List<MultaResponseDTO> resultado = service.obtenerPorTipo("RETRASO");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("RETRASO");
    }

    @Test
    void obtenerEstadoUsuario_conMultasBloqueantes_debeRetornarAvisoBloqueado() {
        when(repository.findByUserId(2L)).thenReturn(List.of(multaEjemplo(), new Multa(2L, 2L, 2L, "dano", LocalDate.of(2024, 1, 2), 4, 1L, "DANO")));
        when(repository.sumCantidadByUserId(2L)).thenReturn(5);

        EstadoMultasDTO estado = service.obtenerEstadoUsuario(2L);

        assertThat(estado.isPuedeReservar()).isFalse();
        assertThat(estado.getAviso()).contains("RESERVA BLOQUEADA");
    }

    @Test
    void guardar_usuarioInexistente_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(adminEjemplo());
        when(userClient.obtenerPorId(2L)).thenThrow(FeignException.NotFound.class);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_historialInexistente_debeLanzarExcepcion() {
        when(userClient.obtenerPorId(1L)).thenReturn(adminEjemplo());
        when(userClient.obtenerPorId(2L)).thenReturn(clienteEjemplo());
        when(historialClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarMulta() {
        when(repository.findById(1L)).thenReturn(Optional.of(multaEjemplo()));
        when(userClient.obtenerPorId(1L)).thenReturn(adminEjemplo());
        when(userClient.obtenerPorId(2L)).thenReturn(clienteEjemplo());
        when(historialClient.obtenerPorId(1L)).thenReturn(new HistorialResponseDTO());
        when(repository.save(any(Multa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MultaResponseDTO resultado = service.actualizar(1L, dtoValido());

        assertThat(resultado.getAdminId()).isEqualTo(1L);
        assertThat(resultado.getUserId()).isEqualTo(2L);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.actualizar(99L, dtoValido()));
    }
    @Test
    void guardar_cuandoServicioAdminCae_debeLanzarExcepcion() {
        FeignException ex = mock(FeignException.class);
        when(userClient.obtenerPorId(1L)).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_cuandoServicioHistorialCae_debeLanzarExcepcionGenerica() {
        FeignException ex = mock(FeignException.class);
        when(userClient.obtenerPorId(1L)).thenReturn(adminEjemplo());
        when(userClient.obtenerPorId(2L)).thenReturn(clienteEjemplo());
        when(historialClient.obtenerPorId(1L)).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }
}