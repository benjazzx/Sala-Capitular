package Biblioteca.example.ReservaLibro.dto;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class EstadoMultasDTO {
    private boolean puedeReservar;
    private int totalMultas;
    private int totalCantidad;
    private String aviso;
}
