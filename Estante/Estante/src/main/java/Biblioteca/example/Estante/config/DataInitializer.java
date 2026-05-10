package Biblioteca.example.Estante.config;

import Biblioteca.example.Estante.model.Estante;
import Biblioteca.example.Estante.repository.EstanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EstanteRepository repository;

    @Override
    public void run(String... args) {
        List<Object[]> estantes = List.of(
            new Object[]{1, 1, "A", 1L},
            new Object[]{2, 1, "A", 2L},
            new Object[]{3, 2, "B", 3L},
            new Object[]{4, 2, "B", 4L},
            new Object[]{5, 1, "C", 5L},
            new Object[]{6, 3, "C", 6L},
            new Object[]{7, 1, "D", 7L}
        );

        for (Object[] datos : estantes) {
            if (!repository.existsByLibroId((Long) datos[3])) {
                repository.save(new Estante(null,
                    (Integer) datos[0],
                    (Integer) datos[1],
                    (String) datos[2],
                    (Long) datos[3]
                ));
            }
        }
    }
}
