package Biblioteca.example.User.controller;

import Biblioteca.example.User.dto.*;
import Biblioteca.example.User.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private UserService service;

    private UserResponseDTO userResp() {
        return new UserResponseDTO(1L, "Juan", "Perez", "juan@mail.com", 2L, "CLIENTE");
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(userResp()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@mail.com"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(userResp()));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void login_credencialesValidas_debeRetornar200() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("juan@mail.com", "pass123");
        when(service.login(any(LoginRequestDTO.class))).thenReturn(userResp());

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rolNombre").value("CLIENTE"));
    }

    @Test
    void login_credencialesInvalidas_debeRetornar400() throws Exception {
        LoginRequestDTO dto = new LoginRequestDTO("juan@mail.com", "wrong");
        when(service.login(any(LoginRequestDTO.class)))
                .thenThrow(new RuntimeException("Credenciales inválidas."));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("Maria", "Lopez", "maria@mail.com", "pass123", 2L);
        when(service.guardar(any(UserRequestDTO.class)))
                .thenReturn(new UserResponseDTO(2L, "Maria", "Lopez", "maria@mail.com", 2L, "CLIENTE"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_emailInvalido_debeRetornar400() throws Exception {
        UserRequestDTO dto = new UserRequestDTO("a", "b", "not-an-email", "pass123", 2L);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
