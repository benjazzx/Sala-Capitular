package Biblioteca.example.Catalogo.controller;

import Biblioteca.example.Catalogo.dto.CatalogoRequestDTO;
import Biblioteca.example.Catalogo.dto.CatalogoResponseDTO;
import Biblioteca.example.Catalogo.service.CatalogoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogoController.class)
class CatalogoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private CatalogoService service;

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(
                new CatalogoResponseDTO(1L, "Novela", "desc")));

        mockMvc.perform(get("/api/catalogos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Novela"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L))
                .thenReturn(Optional.of(new CatalogoResponseDTO(1L, "Novela", "desc")));

        mockMvc.perform(get("/api/catalogos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/catalogos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        CatalogoRequestDTO dto = new CatalogoRequestDTO("Terror", "desc");
        CatalogoResponseDTO resp = new CatalogoResponseDTO(1L, "Terror", "desc");
        when(service.guardar(any(CatalogoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/catalogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Terror"));
    }

    @Test
    void guardar_nombreVacio_debeRetornar400() throws Exception {
        CatalogoRequestDTO dto = new CatalogoRequestDTO("", "desc");

        mockMvc.perform(post("/api/catalogos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        CatalogoRequestDTO dto = new CatalogoRequestDTO("Ciencia Ficcion", "desc");
        CatalogoResponseDTO resp = new CatalogoResponseDTO(1L, "Ciencia Ficcion", "desc");
        when(service.actualizar(eq(1L), any(CatalogoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(put("/api/catalogos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/catalogos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void actualizar_cuandoNoExiste_debeRetornar400() throws Exception {
        CatalogoRequestDTO dto = new CatalogoRequestDTO("X", "y");
        when(service.actualizar(eq(99L), any(CatalogoRequestDTO.class)))
                .thenThrow(new RuntimeException("Catálogo no encontrado con id: 99"));

        mockMvc.perform(put("/api/catalogos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
