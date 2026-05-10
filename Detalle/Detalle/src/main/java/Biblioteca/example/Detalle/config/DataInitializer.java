package Biblioteca.example.Detalle.config;

import Biblioteca.example.Detalle.model.Detalle;
import Biblioteca.example.Detalle.repository.DetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DetalleRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<Detalle> detalles = List.of(
            new Detalle(null, 1L, 1L, "Libro devuelto en buen estado"),
            new Detalle(null, 2L, 2L, "Libro devuelto en buen estado"),
            new Detalle(null, 3L, 3L, "Préstamo activo, pendiente de devolución"),
            new Detalle(null, 4L, 4L, "Libro devuelto con leve desgaste"),
            new Detalle(null, 5L, 5L, "Préstamo activo, pendiente de devolución")
        );

        repository.saveAll(detalles);
    }
}
