package Biblioteca.example.Detalle.controller;

import Biblioteca.example.Detalle.dto.DetalleRequestDTO;
import Biblioteca.example.Detalle.dto.DetalleResponseDTO;
import Biblioteca.example.Detalle.service.DetalleService;
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

@WebMvcTest(DetalleController.class)
class DetalleControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @MockitoBean private DetalleService service;

    private DetalleResponseDTO detalleResp() {
        return new DetalleResponseDTO(1L, 1L, 1L, "Sin observaciones");
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(detalleResp()));

        mockMvc.perform(get("/api/detalles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].observacion").value("Sin observaciones"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(detalleResp()));

        mockMvc.perform(get("/api/detalles/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/detalles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        DetalleRequestDTO dto = new DetalleRequestDTO(1L, 1L, "obs");
        when(service.guardar(any(DetalleRequestDTO.class))).thenReturn(detalleResp());

        mockMvc.perform(post("/api/detalles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_historialNull_debeRetornar400() throws Exception {
        DetalleRequestDTO dto = new DetalleRequestDTO(null, 1L, "obs");

        mockMvc.perform(post("/api/detalles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/detalles/1"))
                .andExpect(status().isNoContent());
    }
}
