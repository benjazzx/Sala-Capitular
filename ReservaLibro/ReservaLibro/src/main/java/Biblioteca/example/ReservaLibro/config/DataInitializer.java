package Biblioteca.example.ReservaLibro.config;

import Biblioteca.example.ReservaLibro.model.ReservaLibro;
import Biblioteca.example.ReservaLibro.repository.ReservaLibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ReservaLibroRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<ReservaLibro> reservas = List.of(
            new ReservaLibro(null, 5L, 6L, LocalDate.of(2026, 5, 1), "ACTIVA"),
            new ReservaLibro(null, 6L, 7L, LocalDate.of(2026, 5, 3), "ACTIVA"),
            new ReservaLibro(null, 7L, 4L, LocalDate.of(2026, 4, 20), "COMPLETADA"),
            new ReservaLibro(null, 5L, 2L, LocalDate.of(2026, 4, 10), "CANCELADA")
        );

        repository.saveAll(reservas);
    }
}
