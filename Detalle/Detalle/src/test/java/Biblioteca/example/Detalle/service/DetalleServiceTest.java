package Biblioteca.example.Detalle.service;

import Biblioteca.example.Detalle.client.HistorialClient;
import Biblioteca.example.Detalle.client.LibroClient;
import Biblioteca.example.Detalle.dto.*;
import Biblioteca.example.Detalle.model.Detalle;
import Biblioteca.example.Detalle.repository.DetalleRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleServiceTest {

    @Mock private DetalleRepository repository;
    @Mock private HistorialClient historialClient;
    @Mock private LibroClient libroClient;

    @InjectMocks private DetalleService service;

    private Detalle detalleEjemplo() {
        return new Detalle(1L, 1L, 1L, "Sin observaciones");
    }

    private DetalleRequestDTO dtoValido() {
        return new DetalleRequestDTO(1L, 1L, "Sin observaciones");
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(detalleEjemplo()));
        List<DetalleResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(detalleEjemplo()));
        Optional<DetalleResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getObservacion()).isEqualTo("Sin observaciones");
    }

    @Test
    void guardar_datosValidos_debeCrear() {
        when(historialClient.obtenerPorId(1L)).thenReturn(new HistorialResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(Detalle.class))).thenReturn(detalleEjemplo());

        DetalleResponseDTO resultado = service.guardar(dtoValido());

        assertThat(resultado.getHistorialId()).isEqualTo(1L);
    }

    @Test
    void guardar_historialInexistente_debeLanzarExcepcion() {
        when(historialClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void guardar_libroInexistente_debeLanzarExcepcion() {
        when(historialClient.obtenerPorId(1L)).thenReturn(new HistorialResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenThrow(FeignException.NotFound.class);
        assertThrows(RuntimeException.class, () -> service.guardar(dtoValido()));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(detalleEjemplo()));
        when(historialClient.obtenerPorId(1L)).thenReturn(new HistorialResponseDTO());
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(Detalle.class))).thenAnswer(i -> i.getArgument(0));
        DetalleResponseDTO resultado = service.actualizar(1L, dtoValido());
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
