package Biblioteca.example.User.service;

import Biblioteca.example.User.client.RolClient;
import Biblioteca.example.User.dto.*;
import Biblioteca.example.User.model.User;
import Biblioteca.example.User.repository.UserRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repository;
    @Mock private RolClient rolClient;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService service;

    private RolResponseDTO rolCliente() {
        return new RolResponseDTO(2L, "CLIENTE", "Usuario cliente");
    }

    private User userEjemplo() {
        return new User(1L, "Juan", "Perez", "juan@mail.com", "$2a$10$hash", 2L);
    }

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(userEjemplo()));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());

        List<UserResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEmail()).isEqualTo("juan@mail.com");
        assertThat(resultado.get(0).getRolNombre()).isEqualTo("CLIENTE");
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar() {
        when(repository.findById(1L)).thenReturn(Optional.of(userEjemplo()));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());

        Optional<UserResponseDTO> resultado = service.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Juan");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarVacio() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void login_credencialesValidas_debeRetornarUser() {
        User user = userEjemplo();
        when(repository.findByEmail("juan@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "$2a$10$hash")).thenReturn(true);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());

        UserResponseDTO resultado = service.login(new LoginRequestDTO("juan@mail.com", "pass123"));

        assertThat(resultado.getEmail()).isEqualTo("juan@mail.com");
    }

    @Test
    void login_emailInexistente_debeLanzarExcepcion() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.login(new LoginRequestDTO("no@mail.com", "pass")));
    }

    @Test
    void login_passwordIncorrecta_debeLanzarExcepcion() {
        when(repository.findByEmail("juan@mail.com")).thenReturn(Optional.of(userEjemplo()));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.login(new LoginRequestDTO("juan@mail.com", "wrong")));
    }

    @Test
    void guardar_rolValido_debeCrearUser() {
        UserRequestDTO dto = new UserRequestDTO("Maria", "Lopez", "maria@mail.com", "pass123", 2L);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());
        when(repository.existsByEmail("maria@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("pass123")).thenReturn("$2a$10$encoded");
        User saved = new User(2L, "Maria", "Lopez", "maria@mail.com", "$2a$10$encoded", 2L);
        when(repository.save(any(User.class))).thenReturn(saved);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());

        UserResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getEmail()).isEqualTo("maria@mail.com");
    }

    @Test
    void guardar_rolAdmin_debeLanzarExcepcion() {
        UserRequestDTO dto = new UserRequestDTO("Admin", "User", "admin@mail.com", "pass123", 1L);
        RolResponseDTO rolAdmin = new RolResponseDTO(1L, "ADMIN", "Administrador");
        when(rolClient.obtenerPorId(1L)).thenReturn(rolAdmin);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.guardar(dto));

        assertThat(ex.getMessage()).contains("ADMIN");
    }

    @Test
    void guardar_emailDuplicado_debeLanzarExcepcion() {
        UserRequestDTO dto = new UserRequestDTO("Juan", "Perez", "juan@mail.com", "pass", 2L);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());
        when(repository.existsByEmail("juan@mail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
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
    void obtenerTodos_cuandoRolNoResponde_debeUsarRolDesconocido() {
        when(repository.findAll()).thenReturn(List.of(userEjemplo()));
        when(rolClient.obtenerPorId(2L)).thenThrow(FeignException.NotFound.class);

        List<UserResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado.get(0).getRolNombre()).isEqualTo("Desconocido");
    }

    @Test
    void guardar_rolInexistente_debeLanzarExcepcion() {
        UserRequestDTO dto = new UserRequestDTO("Ana", "Diaz", "ana@mail.com", "pass123", 99L);
        when(rolClient.obtenerPorId(99L)).thenThrow(FeignException.NotFound.class);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void actualizar_cuandoExiste_debeActualizarUsuario() {
        UserRequestDTO dto = new UserRequestDTO("Juan", "Nuevo", "nuevo@mail.com", "pass123", 2L);
        when(repository.findById(1L)).thenReturn(Optional.of(userEjemplo()));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente());
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO resultado = service.actualizar(1L, dto);

        assertThat(resultado.getApellido()).isEqualTo("Nuevo");
        assertThat(resultado.getEmail()).isEqualTo("nuevo@mail.com");
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.actualizar(99L, new UserRequestDTO("Ana", "Diaz", "ana@mail.com", "pass123", 2L)));
    }
}