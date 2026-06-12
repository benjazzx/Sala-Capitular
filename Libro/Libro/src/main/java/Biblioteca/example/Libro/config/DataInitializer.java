package Biblioteca.example.Libro.config;

import Biblioteca.example.Libro.model.Libro;
import Biblioteca.example.Libro.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LibroRepository repository;

    @Override
    public void run(String... args) {
        List<Object[]> libros = List.of(
            new Object[]{"Cien años de soledad", "978-0060883287", 1967, "Obra maestra del realismo mágico", 4L, 3L, 1L},
            new Object[]{"Veinte poemas de amor", "978-9562890823", 1924, "Colección de poemas de amor", 3L, 3L, 1L},
            new Object[]{"La casa de los espíritus", "978-0553383805", 1982, "Novela histórica familiar", 2L, 1L, 1L},
            new Object[]{"Fundación", "978-0553293357", 1951, "Saga de ciencia ficción galáctica", 4L, 1L, 1L},
            new Object[]{"Dune", "978-0441013593", 1965, "Épica de ciencia ficción en desierto", 2L, 1L, 1L},
            new Object[]{"El nombre de la rosa", "978-0151446476", 1980, "Misterio medieval en monasterio", 3L, 4L, 1L},
            new Object[]{"2001: Odisea del espacio", "978-0451457998", 1968, "Viaje espacial y evolución humana", 2L, 1L, 1L}
        );

        for (Object[] datos : libros) {
            if (!repository.existsByIsbn((String) datos[1])) {
                repository.save(new Libro(null,
                    (String) datos[0],
                    (String) datos[1],
                    (Integer) datos[2],
                    (String) datos[3],
                    (Long) datos[4],
                    (Long) datos[5],
                    (Long) datos[6]
                ));
            }
        }
    }
}
