package Biblioteca.example.ResenaLibro.controller;

import Biblioteca.example.ResenaLibro.dto.ResenaLibroRequestDTO;
import Biblioteca.example.ResenaLibro.dto.ResenaLibroResponseDTO;
import Biblioteca.example.ResenaLibro.service.ResenaLibroService;
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

@WebMvcTest(ResenaLibroController.class)
class ResenaLibroControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ResenaLibroService service;

    private ResenaLibroResponseDTO resenaResp() {
        return new ResenaLibroResponseDTO(1L, 1L, 1L, 5, "Excelente", LocalDate.of(2024, 3, 15));
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(resenaResp()));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].calificacion").value(5));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(resenaResp()));

        mockMvc.perform(get("/api/resenas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/resenas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorLibro_debeRetornar200() throws Exception {
        when(service.obtenerPorLibro(1L)).thenReturn(List.of(resenaResp()));

        mockMvc.perform(get("/api/resenas/libro/1"))
                .andExpect(status().isOk());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        ResenaLibroRequestDTO dto = new ResenaLibroRequestDTO(1L, 1L, 5, "Excelente", LocalDate.now());
        when(service.guardar(any(ResenaLibroRequestDTO.class))).thenReturn(resenaResp());

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_calificacionInvalida_debeRetornar400() throws Exception {
        ResenaLibroRequestDTO dto = new ResenaLibroRequestDTO(1L, 1L, 10, "comentario", LocalDate.now());

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/resenas/1"))
                .andExpect(status().isNoContent());
    }
}
