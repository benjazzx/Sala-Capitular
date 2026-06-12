package Biblioteca.example.Rol.service;

import Biblioteca.example.Rol.dto.RolRequestDTO;
import Biblioteca.example.Rol.dto.RolResponseDTO;
import Biblioteca.example.Rol.model.Rol;
import Biblioteca.example.Rol.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository repository;

    @InjectMocks
    private RolService service;

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ADMIN", "Administrador del sistema");
    }

    @Test
    void obtenerTodos_debeRetornarListaDeRoles() {
        Rol rol2 = new Rol(2L, "CLIENTE", "Cliente de la biblioteca");
        when(repository.findAll()).thenReturn(Arrays.asList(rol, rol2));

        List<RolResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("ADMIN");
        assertThat(resultado.get(1).getNombre()).isEqualTo("CLIENTE");
        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerTodos_debeRetornarListaVaciaSiNoHayRoles() {
        when(repository.findAll()).thenReturn(List.of());

        List<RolResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorId_debeRetornarRolCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(rol));

        Optional<RolResponseDTO> resultado = service.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("ADMIN");
    }

    @Test
    void obtenerPorId_debeRetornarVacioCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<RolResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_debeCrearYRetornarRol() {
        RolRequestDTO dto = new RolRequestDTO("AUTOR", "Autor de libros");
        Rol rolGuardado = new Rol(3L, "AUTOR", "Autor de libros");
        when(repository.save(any(Rol.class))).thenReturn(rolGuardado);

        RolResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("AUTOR");
        verify(repository, times(1)).save(any(Rol.class));
    }

    @Test
    void actualizar_debeActualizarRolExistente() {
        RolRequestDTO dto = new RolRequestDTO("ADMIN", "Nueva descripcion");
        when(repository.findById(1L)).thenReturn(Optional.of(rol));
        when(repository.save(any(Rol.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RolResponseDTO resultado = service.actualizar(1L, dto);

        assertThat(resultado.getDescripcion()).isEqualTo("Nueva descripcion");
        verify(repository, times(1)).save(any(Rol.class));
    }

    @Test
    void actualizar_debeLanzarExcepcionSiRolNoExiste() {
        RolRequestDTO dto = new RolRequestDTO("ADMIN", "Nueva descripcion");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.actualizar(99L, dto));
        verify(repository, never()).save(any(Rol.class));
    }

    @Test
    void eliminar_debeEliminarRolExistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcionSiRolNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
