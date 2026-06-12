package Biblioteca.example.Rol.config;

import Biblioteca.example.Rol.model.Rol;
import Biblioteca.example.Rol.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository repository;

    @Override
    public void run(String... args) {
        List<String[]> roles = List.of(
            new String[]{"CLIENTE", "Usuario que puede reservar y consultar libros"},
            new String[]{"ADMIN", "Administrador con acceso a funciones de gestión"},
            new String[]{"AUTOR", "Autor registrado que puede publicar libros"}
        );

        for (String[] datos : roles) {
            if (!repository.existsByNombre(datos[0])) {
                repository.save(new Rol(null, datos[0], datos[1]));
            }
        }
    }
}
