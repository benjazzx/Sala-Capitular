package Biblioteca.example.Estado.config;

import Biblioteca.example.Estado.model.Estado;
import Biblioteca.example.Estado.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EstadoRepository repository;

    @Override
    public void run(String... args) {
        List<String[]> estados = List.of(
            new String[]{"DISPONIBLE", "El libro está disponible para préstamo o reserva"},
            new String[]{"RESERVADO", "El libro tiene una reserva activa"},
            new String[]{"PRESTADO", "El libro está actualmente prestado"},
            new String[]{"DAÑADO", "El libro presenta daños y no está disponible"}
        );

        for (String[] datos : estados) {
            if (!repository.existsByNombre(datos[0])) {
                repository.save(new Estado(null, datos[0], datos[1]));
            }
        }
    }
}
