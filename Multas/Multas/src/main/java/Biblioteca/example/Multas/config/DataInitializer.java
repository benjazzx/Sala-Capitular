package Biblioteca.example.Multas.config;

import Biblioteca.example.Multas.model.Multa;
import Biblioteca.example.Multas.repository.MultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MultaRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<Multa> multas = List.of(
            new Multa(null, 7L, 3L, "Devolución tardía", LocalDate.of(2026, 3, 20), 1),
            new Multa(null, 6L, 5L, "Daño en cubierta del libro", LocalDate.of(2026, 4, 5), 2),
            new Multa(null, 5L, 1L, "Devolución tardía", LocalDate.of(2026, 2, 1), 1)
        );

        repository.saveAll(multas);
    }
}
