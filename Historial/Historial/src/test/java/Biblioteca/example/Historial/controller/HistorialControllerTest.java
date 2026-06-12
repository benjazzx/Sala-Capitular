package Biblioteca.example.Historial.controller;

import Biblioteca.example.Historial.dto.HistorialRequestDTO;
import Biblioteca.example.Historial.dto.HistorialResponseDTO;
import Biblioteca.example.Historial.service.HistorialService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HistorialController.class)
class HistorialControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private HistorialService service;

    private HistorialResponseDTO historialResp() {
        return new HistorialResponseDTO(1L, 1L, 1L, LocalDate.of(2024, 1, 10), null, 1L);
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(historialResp()));

        mockMvc.perform(get("/api/historiales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(historialResp()));

        mockMvc.perform(get("/api/historiales/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/historiales/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        HistorialRequestDTO dto = new HistorialRequestDTO(1L, 1L, LocalDate.now(), null, 1L);
        when(service.guardar(any(HistorialRequestDTO.class))).thenReturn(historialResp());

        mockMvc.perform(post("/api/historiales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_servicioLanzaExcepcion_debeRetornar400() throws Exception {
        HistorialRequestDTO dto = new HistorialRequestDTO(1L, 1L, LocalDate.now(), null, 1L);
        when(service.guardar(any(HistorialRequestDTO.class)))
                .thenThrow(new RuntimeException("El libro no existe con id: 1"));

        mockMvc.perform(post("/api/historiales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/historiales/1"))
                .andExpect(status().isNoContent());
    }
}
