package Biblioteca.example.ReservaLibro.controller;

import Biblioteca.example.ReservaLibro.dto.ReservaLibroRequestDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroResponseDTO;
import Biblioteca.example.ReservaLibro.service.ReservaLibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaLibroController.class)
class ReservaLibroControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private ReservaLibroService service;

    private ReservaLibroResponseDTO reservaResp() {
        return new ReservaLibroResponseDTO(1L, 1L, 1L, LocalDate.of(2024, 6, 1), "ACTIVA", null);
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(reservaResp()));

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estadoReserva").value("ACTIVA"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(reservaResp()));

        mockMvc.perform(get("/api/reservas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        ReservaLibroRequestDTO dto = new ReservaLibroRequestDTO(1L, 1L, LocalDate.now(), "ACTIVA");
        when(service.guardar(any(ReservaLibroRequestDTO.class))).thenReturn(reservaResp());

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_estadoInvalido_debeRetornar400() throws Exception {
        ReservaLibroRequestDTO dto = new ReservaLibroRequestDTO(1L, 1L, LocalDate.now(), "INVALIDO");

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardar_usuarioBloqueado_debeRetornar400() throws Exception {
        ReservaLibroRequestDTO dto = new ReservaLibroRequestDTO(1L, 1L, LocalDate.now(), "ACTIVA");
        when(service.guardar(any(ReservaLibroRequestDTO.class)))
                .thenThrow(new RuntimeException("RESERVA BLOQUEADA."));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/reservas/1"))
                .andExpect(status().isNoContent());
    }
}
