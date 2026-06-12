package Biblioteca.example.ResenaLibro.config;

import Biblioteca.example.ResenaLibro.model.ResenaLibro;
import Biblioteca.example.ResenaLibro.repository.ResenaLibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ResenaLibroRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        List<ResenaLibro> resenas = List.of(
            new ResenaLibro(null, 5L, 1L, 5, "Una obra maestra, imprescindible.", LocalDate.of(2026, 1, 25)),
            new ResenaLibro(null, 6L, 2L, 5, "Poesía que toca el alma.", LocalDate.of(2026, 2, 20)),
            new ResenaLibro(null, 7L, 3L, 4, "Muy buena novela histórica.", LocalDate.of(2026, 3, 10)),
            new ResenaLibro(null, 5L, 4L, 5, "La mejor saga de ciencia ficción.", LocalDate.of(2026, 3, 30)),
            new ResenaLibro(null, 6L, 5L, 4, "Dune es un clásico del género.", LocalDate.of(2026, 4, 12)),
            new ResenaLibro(null, 7L, 6L, 3, "Interesante pero algo lento al inicio.", LocalDate.of(2026, 4, 20))
        );

        repository.saveAll(resenas);
    }
}
