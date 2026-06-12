package Biblioteca.example.Libro.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(unique = true, length = 20)
    private String isbn;

    private Integer anio;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "autor_id", nullable = false)
    private Long autorId;

    @Column(name = "catalogo_id", nullable = false)
    private Long catalogoId;

    @Column(name = "estado_id", nullable = false)
    private Long estadoId;
}
