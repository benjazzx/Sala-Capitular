package Biblioteca.example.Estado.controller;

import Biblioteca.example.Estado.dto.EstadoRequestDTO;
import Biblioteca.example.Estado.dto.EstadoResponseDTO;
import Biblioteca.example.Estado.service.EstadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadoController.class)
class EstadoControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private EstadoService service;

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(
                new EstadoResponseDTO(1L, "DISPONIBLE", "desc")));

        mockMvc.perform(get("/api/estados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("DISPONIBLE"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L))
                .thenReturn(Optional.of(new EstadoResponseDTO(1L, "PRESTADO", "desc")));

        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estados/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        EstadoRequestDTO dto = new EstadoRequestDTO("DISPONIBLE", "desc");
        EstadoResponseDTO resp = new EstadoResponseDTO(1L, "DISPONIBLE", "desc");
        when(service.guardar(any(EstadoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        EstadoRequestDTO dto = new EstadoRequestDTO("PRESTADO", "desc");
        EstadoResponseDTO resp = new EstadoResponseDTO(1L, "PRESTADO", "desc");
        when(service.actualizar(eq(1L), any(EstadoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(put("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/estados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_cuandoNoExiste_debeRetornar400() throws Exception {
        doThrow(new RuntimeException("no encontrado")).when(service).eliminar(99L);

        mockMvc.perform(delete("/api/estados/99"))
                .andExpect(status().isBadRequest());
    }
}
