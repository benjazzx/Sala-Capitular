package Biblioteca.example.Rol.service;

import Biblioteca.example.Rol.dto.RolRequestDTO;
import Biblioteca.example.Rol.dto.RolResponseDTO;
import Biblioteca.example.Rol.model.Rol;
import Biblioteca.example.Rol.repository.RolRepository;
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
class RolServiceTest {

    @Mock
    private RolRepository repository;

    @InjectMocks
    private RolService service;

    @Test
    void obtenerTodos_debeRetornarListaDeRoles() {
        Rol rol = new Rol(1L, "CLIENTE", "Usuario cliente");
        when(repository.findAll()).thenReturn(List.of(rol));

        List<RolResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("CLIENTE");
        verify(repository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornarRol() {
        Rol rol = new Rol(1L, "ADMIN", "Administrador");
        when(repository.findById(1L)).thenReturn(Optional.of(rol));

        Optional<RolResponseDTO> resultado = service.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("ADMIN");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<RolResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_debeCrearRol() {
        RolRequestDTO dto = new RolRequestDTO("AUTOR", "Autor de libros");
        Rol saved = new Rol(1L, "AUTOR", "Autor de libros");
        when(repository.save(any(Rol.class))).thenReturn(saved);

        RolResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("AUTOR");
    }

    @Test
    void actualizar_cuandoExiste_debeActualizar() {
        Rol rol = new Rol(1L, "CLIENTE", "desc");
        RolRequestDTO dto = new RolRequestDTO("ADMIN", "Administrador");
        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        when(repository.save(any(Rol.class))).thenAnswer(i -> i.getArgument(0));

        RolResponseDTO resultado = service.actualizar(1L, dto);

        assertThat(resultado.getNombre()).isEqualTo("ADMIN");
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, new RolRequestDTO("ADMIN", "desc")));

        assertThat(ex.getMessage()).contains("99");
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
