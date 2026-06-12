package Biblioteca.example.Multas.controller;

import Biblioteca.example.Multas.dto.*;
import Biblioteca.example.Multas.service.MultaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MultaController.class)
class MultaControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private MultaService service;

    private MultaResponseDTO multaResp() {
        return new MultaResponseDTO(1L, 1L, 2L, 1L, "desc", LocalDate.of(2024, 1, 1), 1, "RETRASO");
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(multaResp()));

        mockMvc.perform(get("/api/multas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("RETRASO"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(multaResp()));

        mockMvc.perform(get("/api/multas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/multas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorUsuario_debeRetornar200() throws Exception {
        when(service.obtenerPorUsuario(2L)).thenReturn(List.of(multaResp()));

        mockMvc.perform(get("/api/multas/usuario/2"))
                .andExpect(status().isOk());
    }

    @Test
    void puedeReservar_debeRetornar200() throws Exception {
        when(service.puedeReservar(2L)).thenReturn(true);

        mockMvc.perform(get("/api/multas/usuario/2/puede-pedir"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void obtenerEstadoUsuario_debeRetornar200() throws Exception {
        when(service.obtenerEstadoUsuario(2L))
                .thenReturn(new EstadoMultasDTO(true, 0, 0, "Sin multas pendientes."));

        mockMvc.perform(get("/api/multas/usuario/2/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.puedeReservar").value(true));
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        MultaRequestDTO dto = new MultaRequestDTO(1L, 2L, 1L, "retraso",
                LocalDate.of(2024, 1, 1), 1, "RETRASO");
        when(service.guardar(any(MultaRequestDTO.class))).thenReturn(multaResp());

        mockMvc.perform(post("/api/multas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_tipoInvalido_debeRetornar400() throws Exception {
        MultaRequestDTO dto = new MultaRequestDTO(1L, 2L, 1L, "desc",
                LocalDate.of(2024, 1, 1), 1, "INVALIDO");

        mockMvc.perform(post("/api/multas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/multas/1"))
                .andExpect(status().isNoContent());
    }
}
