package Biblioteca.example.User.service;

import Biblioteca.example.User.client.RolClient;
import Biblioteca.example.User.dto.LoginRequestDTO;
import Biblioteca.example.User.dto.RolResponseDTO;
import Biblioteca.example.User.dto.UserRequestDTO;
import Biblioteca.example.User.dto.UserResponseDTO;
import Biblioteca.example.User.model.User;
import Biblioteca.example.User.repository.UserRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private RolClient rolClient;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private User user;
    private RolResponseDTO rolCliente;
    private RolResponseDTO rolAdmin;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Juan", "Perez", "juan@correo.com", "hashed-pass", 2L);
        rolCliente = new RolResponseDTO(2L, "CLIENTE", "Cliente de la biblioteca");
        rolAdmin = new RolResponseDTO(1L, "ADMIN", "Administrador");
    }

    private FeignException notFoundException() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/roles/99",
                java.util.Collections.emptyMap(), null, new RequestTemplate());
        return new FeignException.NotFound("Not Found", request, null, null);
    }

    @Test
    void obtenerTodos_debeRetornarListaDeUsuarios() {
        when(repository.findAll()).thenReturn(List.of(user));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);

        List<UserResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRolNombre()).isEqualTo("CLIENTE");
    }

    @Test
    void obtenerTodos_debeManejarErrorDeFeign() {
        when(repository.findAll()).thenReturn(List.of(user));
        when(rolClient.obtenerPorId(2L)).thenThrow(notFoundException());

        List<UserResponseDTO> resultado = service.obtenerTodos();

        assertThat(resultado.get(0).getRolNombre()).isEqualTo("Desconocido");
    }

    @Test
    void obtenerPorId_debeRetornarUsuarioCuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);

        Optional<UserResponseDTO> resultado = service.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("juan@correo.com");
    }

    @Test
    void obtenerPorId_debeRetornarVacioCuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> resultado = service.obtenerPorId(99L);

        assertThat(resultado).isEmpty();
        verify(rolClient, never()).obtenerPorId(any());
    }

    @Test
    void login_debeRetornarUsuarioConCredencialesValidas() {
        LoginRequestDTO dto = new LoginRequestDTO("juan@correo.com", "password123");
        when(repository.findByEmail("juan@correo.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-pass")).thenReturn(true);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);

        UserResponseDTO resultado = service.login(dto);

        assertThat(resultado.getEmail()).isEqualTo("juan@correo.com");
    }

    @Test
    void login_debeLanzarExcepcionSiEmailNoExiste() {
        LoginRequestDTO dto = new LoginRequestDTO("inexistente@correo.com", "password123");
        when(repository.findByEmail("inexistente@correo.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.login(dto));
    }

    @Test
    void login_debeLanzarExcepcionSiPasswordIncorrecta() {
        LoginRequestDTO dto = new LoginRequestDTO("juan@correo.com", "wrongpass");
        when(repository.findByEmail("juan@correo.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashed-pass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.login(dto));
    }

    @Test
    void guardar_debeCrearUsuarioConRolValido() {
        UserRequestDTO dto = new UserRequestDTO("Ana", "Lopez", "ana@correo.com", "password123", 2L);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);
        when(repository.existsByEmail("ana@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-pass");
        User saved = new User(2L, "Ana", "Lopez", "ana@correo.com", "encoded-pass", 2L);
        when(repository.save(any(User.class))).thenReturn(saved);

        UserResponseDTO resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(2L);
        assertThat(resultado.getEmail()).isEqualTo("ana@correo.com");
    }

    @Test
    void guardar_debeLanzarExcepcionSiRolEsAdmin() {
        UserRequestDTO dto = new UserRequestDTO("Ana", "Lopez", "ana@correo.com", "password123", 1L);
        when(rolClient.obtenerPorId(1L)).thenReturn(rolAdmin);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void guardar_debeLanzarExcepcionSiEmailYaExiste() {
        UserRequestDTO dto = new UserRequestDTO("Ana", "Lopez", "ana@correo.com", "password123", 2L);
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);
        when(repository.existsByEmail("ana@correo.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void guardar_debeLanzarExcepcionSiRolNoExiste() {
        UserRequestDTO dto = new UserRequestDTO("Ana", "Lopez", "ana@correo.com", "password123", 99L);
        when(rolClient.obtenerPorId(99L)).thenThrow(notFoundException());

        assertThrows(RuntimeException.class, () -> service.guardar(dto));
    }

    @Test
    void actualizar_debeActualizarUsuarioExistente() {
        UserRequestDTO dto = new UserRequestDTO("Juan", "Perez", "juan2@correo.com", "password123", 2L);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(rolClient.obtenerPorId(2L)).thenReturn(rolCliente);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO resultado = service.actualizar(1L, dto);

        assertThat(resultado.getEmail()).isEqualTo("juan2@correo.com");
    }

    @Test
    void actualizar_debeLanzarExcepcionSiUsuarioNoExiste() {
        UserRequestDTO dto = new UserRequestDTO("Juan", "Perez", "juan2@correo.com", "password123", 2L);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.actualizar(99L, dto));
    }

    @Test
    void eliminar_debeEliminarUsuarioExistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_debeLanzarExcepcionSiUsuarioNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.eliminar(99L));
    }
}
