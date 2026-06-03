package Biblioteca.example.Catalogo.service;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.model.Catalogo;
import Biblioteca.example.Catalogo.repository.CatalogoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {

    @Mock
    private CatalogoRepository repository;

    @InjectMocks
    private CatalogoService service;

    @Test
    void obtenerTodos_debeRetornarListaDeCatalogos() {
        // Arrange
        Catalogo catalogo = new Catalogo(
                1L,
                "Novela",
                "Libros de narrativa");

        when(repository.findAll()).thenReturn(List.of(catalogo));

        // Act
        List<CatalogoResponseDTO> resultado = service.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Novela");
        assertThat(resultado.get(0).getDescripcion()).isEqualTo("Libros de narrativa");

        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarCatalogo() {
        // Arrange
        Long id = 1L;

        Catalogo catalogo = new Catalogo(
                id,
                "Terror",
                "Libros de terror");

        when(repository.findById(id)).thenReturn(Optional.of(catalogo));

        // Act
        Optional<CatalogoResponseDTO> resultado = service.obtenerPorId(id);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getNombre()).isEqualTo("Terror");
        assertThat(resultado.get().getDescripcion()).isEqualTo("Libros de terror");

        verify(repository, times(1)).findById(id);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarOptionalVacio() {
        // Arrange
        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<CatalogoResponseDTO> resultado = service.obtenerPorId(id);

        // Assert
        assertThat(resultado).isEmpty();

        verify(repository, times(1)).findById(id);
    }

    @Test
    void guardar_cuandoDatosValidos_debeCrearCatalogo() {
        // Arrange
        CatalogoRequestDTO request = new CatalogoRequestDTO(
                "Ciencia ficción",
                "Libros de ciencia ficción");

        Catalogo catalogoGuardado = new Catalogo(
                1L,
                "Ciencia ficción",
                "Libros de ciencia ficción");

        when(repository.save(any(Catalogo.class))).thenReturn(catalogoGuardado);

        // Act
        CatalogoResponseDTO resultado = service.guardar(request);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Ciencia ficción");
        assertThat(resultado.getDescripcion()).isEqualTo("Libros de ciencia ficción");

        verify(repository, times(1)).save(any(Catalogo.class));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarCatalogo() {
        // Arrange
        Long id = 1L;

        Catalogo catalogoExistente = new Catalogo(
                id,
                "Nombre antiguo",
                "Descripción antigua");

        CatalogoRequestDTO request = new CatalogoRequestDTO(
                "Nombre actualizado",
                "Descripción actualizada");

        when(repository.findById(id)).thenReturn(Optional.of(catalogoExistente));
        when(repository.save(any(Catalogo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CatalogoResponseDTO resultado = service.actualizar(id, request);

        // Assert
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Nombre actualizado");
        assertThat(resultado.getDescripcion()).isEqualTo("Descripción actualizada");

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).save(catalogoExistente);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        // Arrange
        Long id = 99L;

        CatalogoRequestDTO request = new CatalogoRequestDTO(
                "Nuevo nombre",
                "Nueva descripción");

        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.actualizar(id, request);
        });

        // Assert
        assertThat(exception.getMessage()).contains("Catálogo no encontrado con id: 99");

        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(Catalogo.class));
    }

    @Test
    void eliminar_cuandoExiste_debeEliminarCatalogo() {
        // Arrange
        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        // Act
        service.eliminar(id);

        // Assert
        verify(repository, times(1)).existsById(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcion() {
        // Arrange
        Long id = 99L;

        when(repository.existsById(id)).thenReturn(false);

        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.eliminar(id);
        });

        // Assert
        assertThat(exception.getMessage()).contains("Catálogo no encontrado con id: 99");

        verify(repository, times(1)).existsById(id);
        verify(repository, never()).deleteById(anyLong());
    }

}