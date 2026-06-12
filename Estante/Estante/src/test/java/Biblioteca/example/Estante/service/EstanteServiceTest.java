package Biblioteca.example.Estante.service;

import Biblioteca.example.Estante.client.LibroClient;
import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.dto.LibroResponseDTO;
import Biblioteca.example.Estante.model.Estante;
import Biblioteca.example.Estante.repository.EstanteRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstanteServiceTest {

    @Mock
    private EstanteRepository repository;

    @Mock
    private LibroClient libroClient;

    @InjectMocks
    private EstanteService service;

    private Estante estanteEjemplo() {
        return new Estante(1L, 3, 2, "A", 1L);
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(estanteEjemplo()));

        List<EstanteResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPasillo()).isEqualTo("A");
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarEstante() {
        when(repository.findById(1L)).thenReturn(Optional.of(estanteEjemplo()));

        Optional<EstanteResponseDTO> resultado = service.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNumero()).isEqualTo(3);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<EstanteResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_cuandoLibroExiste_debeCrearEstante() {
        EstanteRequestDTO dto = new EstanteRequestDTO(3, 2, "A", 1L);
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(Estante.class))).thenReturn(estanteEjemplo());

        EstanteResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getLibroId()).isEqualTo(1L);
    }

    @Test
    void guardar_cuandoLibroNoExiste_debeLanzarExcepcion() {
        EstanteRequestDTO dto = new EstanteRequestDTO(3, 2, "A", 99L);
        when(libroClient.obtenerPorId(99L)).thenThrow(FeignException.NotFound.class);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarEstante() {
        EstanteRequestDTO dto = new EstanteRequestDTO(5, 3, "B", 1L);
        when(repository.findById(1L)).thenReturn(Optional.of(estanteEjemplo()));
        when(libroClient.obtenerPorId(1L)).thenReturn(new LibroResponseDTO());
        when(repository.save(any(Estante.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EstanteResponseDTO resultado = service.actualizar(1L, dto);

        assertThat(resultado.getNumero()).isEqualTo(5);
        assertThat(resultado.getPasillo()).isEqualTo("B");
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, new EstanteRequestDTO(1, 1, "X", 1L)));
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
