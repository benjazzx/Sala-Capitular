package Biblioteca.example.Catalogo.config;

import Biblioteca.example.Catalogo.model.Catalogo;
import Biblioteca.example.Catalogo.repository.CatalogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CatalogoRepository repository;

    @Override
    public void run(String... args) {
        List<String[]> catalogos = List.of(
            new String[]{"Ciencia Ficción", "Libros de ciencia ficción y futurismo"},
            new String[]{"Terror", "Libros de terror y suspenso"},
            new String[]{"Romance", "Libros de romance y drama"},
            new String[]{"Historia", "Libros históricos y biografías"},
            new String[]{"Tecnología", "Libros de tecnología e informática"},
            new String[]{"Filosofía", "Libros de filosofía y pensamiento"},
            new String[]{"Aventura", "Libros de aventura y acción"}
        );

        for (String[] datos : catalogos) {
            if (!repository.existsByNombre(datos[0])) {
                repository.save(new Catalogo(null, datos[0], datos[1]));
            }
        }
    }
}
