package Biblioteca.example.Estado.service;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.model.Estado;
import Biblioteca.example.Estado.repository.EstadoRepository;
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
class EstadoServiceTest {

    @Mock private EstadoRepository repository;
    @InjectMocks private EstadoService service;

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(new Estado(1L, "DISPONIBLE", "desc")));
        List<EstadoResponseDTO> resultado = service.obtenerTodos();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("DISPONIBLE");
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Estado(1L, "PRESTADO", "desc")));
        Optional<EstadoResponseDTO> resultado = service.obtenerPorId(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("PRESTADO");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThat(service.obtenerPorId(99L)).isEmpty();
    }

    @Test
    void guardar_debeCrearEstado() {
        EstadoRequestDTO dto = new EstadoRequestDTO("DISPONIBLE", "Libro disponible");
        Estado saved = new Estado(1L, "DISPONIBLE", "Libro disponible");
        when(repository.save(any(Estado.class))).thenReturn(saved);
        EstadoResponseDTO resultado = service.guardar(dto);
        assertThat(resultado.getNombre()).isEqualTo("DISPONIBLE");
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        Estado estado = new Estado(1L, "DISPONIBLE", "desc");
        EstadoRequestDTO dto = new EstadoRequestDTO("PRESTADO", "Libro prestado");
        when(repository.findById(1L)).thenReturn(Optional.of(estado));
        when(repository.save(any(Estado.class))).thenAnswer(i -> i.getArgument(0));
        EstadoResponseDTO resultado = service.actualizar(1L, dto);
        assertThat(resultado.getNombre()).isEqualTo("PRESTADO");
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.actualizar(99L, new EstadoRequestDTO("X", "y")));
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
        verify(repository, never()).deleteById(any());
    }
}
