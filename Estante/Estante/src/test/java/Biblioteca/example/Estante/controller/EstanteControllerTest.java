package Biblioteca.example.Estante.controller;

import Biblioteca.example.Estante.dto.EstanteRequestDTO;
import Biblioteca.example.Estante.dto.EstanteResponseDTO;
import Biblioteca.example.Estante.service.EstanteService;
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

@WebMvcTest(EstanteController.class)
class EstanteControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private EstanteService service;

    private EstanteResponseDTO estanteResp() {
        return new EstanteResponseDTO(1L, 3, 2, "A", 1L);
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(estanteResp()));

        mockMvc.perform(get("/api/estantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pasillo").value("A"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(estanteResp()));

        mockMvc.perform(get("/api/estantes/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/estantes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        EstanteRequestDTO dto = new EstanteRequestDTO(3, 2, "A", 1L);
        when(service.guardar(any(EstanteRequestDTO.class))).thenReturn(estanteResp());

        mockMvc.perform(post("/api/estantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_servicioLanzaExcepcion_debeRetornar400() throws Exception {
        EstanteRequestDTO dto = new EstanteRequestDTO(3, 2, "A", 99L);
        when(service.guardar(any(EstanteRequestDTO.class)))
                .thenThrow(new RuntimeException("Libro no existe con id: 99"));

        mockMvc.perform(post("/api/estantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/estantes/1"))
                .andExpect(status().isNoContent());
    }
}
