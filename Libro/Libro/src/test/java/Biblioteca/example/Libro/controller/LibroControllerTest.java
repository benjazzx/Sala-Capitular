package Biblioteca.example.Libro.controller;

import Biblioteca.example.Libro.dto.LibroRequestDTO;
import Biblioteca.example.Libro.dto.LibroResponseDTO;
import Biblioteca.example.Libro.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
class LibroControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private LibroService service;

    private LibroResponseDTO libroResp() {
        return new LibroResponseDTO(1L, "El Quijote", "ISBN001", 1605, "desc", 2L, 1L, 1L);
    }

    @Test
    void obtenerTodos_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(libroResp()));

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("El Quijote"));
    }

    @Test
    void obtenerPorId_cuandoExiste_debeRetornar200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(Optional.of(libroResp()));

        mockMvc.perform(get("/api/libros/1"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornar404() throws Exception {
        when(service.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/libros/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerPorAutor_debeRetornar200() throws Exception {
        when(service.obtenerPorAutor(2L)).thenReturn(List.of(libroResp()));

        mockMvc.perform(get("/api/libros/autor/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("ISBN001"));
    }

    @Test
    void guardar_datosValidos_debeRetornar201() throws Exception {
        LibroRequestDTO dto = new LibroRequestDTO("El Quijote", "ISBN001", 1605, "desc", 2L, 1L, 1L);
        when(service.guardar(any(LibroRequestDTO.class))).thenReturn(libroResp());

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_tituloVacio_debeRetornar400() throws Exception {
        LibroRequestDTO dto = new LibroRequestDTO("", null, null, null, 2L, 1L, 1L);

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        LibroRequestDTO dto = new LibroRequestDTO("Nuevo Titulo", "ISBN002", 2000, "desc", 2L, 1L, 1L);
        when(service.actualizar(eq(1L), any(LibroRequestDTO.class))).thenReturn(libroResp());

        mockMvc.perform(put("/api/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/libros/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void guardar_servicioLanzaExcepcion_debeRetornar400() throws Exception {
        LibroRequestDTO dto = new LibroRequestDTO("Titulo", "ISBN001", 2020, "desc", 2L, 1L, 1L);
        when(service.guardar(any(LibroRequestDTO.class)))
                .thenThrow(new RuntimeException("ISBN duplicado"));

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
