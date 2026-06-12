package Biblioteca.example.User.config;

import Biblioteca.example.User.model.User;
import Biblioteca.example.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<Object[]> usuarios = List.of(
            new Object[]{"Admin",   "Sistema",  "admin@biblioteca.cl",              "admin123",    2L},
            new Object[]{"Gabriel", "Mistral",  "gabriela.mistral@biblioteca.cl",   "autor123",    3L},
            new Object[]{"Pablo",   "Neruda",   "pablo.neruda@biblioteca.cl",       "autor123",    3L},
            new Object[]{"Isabel",  "Allende",  "isabel.allende@biblioteca.cl",     "autor123",    3L},
            new Object[]{"Juan",    "Pérez",    "juan.perez@biblioteca.cl",         "cliente123",  1L},
            new Object[]{"María",   "González", "maria.gonzalez@biblioteca.cl",     "cliente123",  1L},
            new Object[]{"Carlos",  "López",    "carlos.lopez@biblioteca.cl",       "cliente123",  1L}
        );

        for (Object[] datos : usuarios) {
            if (!repository.existsByEmail((String) datos[2])) {
                repository.save(new User(null,
                    (String) datos[0],
                    (String) datos[1],
                    (String) datos[2],
                    passwordEncoder.encode((String) datos[3]),
                    (Long) datos[4]
                ));
            }
        }
    }
}
