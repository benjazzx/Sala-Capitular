package Biblioteca.example.Historial.config;

import Biblioteca.example.Historial.model.Historial;
import Biblioteca.example.Historial.repository.HistorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HistorialRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<Historial> historiales = List.of(
            new Historial(null, 5L, 1L, LocalDate.of(2026, 1, 10), LocalDate.of(2026, 1, 24), 1L),
            new Historial(null, 6L, 2L, LocalDate.of(2026, 2, 5), LocalDate.of(2026, 2, 19), 1L),
            new Historial(null, 7L, 3L, LocalDate.of(2026, 3, 1), null, 3L),
            new Historial(null, 5L, 4L, LocalDate.of(2026, 3, 15), LocalDate.of(2026, 3, 29), 1L),
            new Historial(null, 6L, 5L, LocalDate.of(2026, 4, 1), null, 3L)
        );

        repository.saveAll(historiales);
    }
}
