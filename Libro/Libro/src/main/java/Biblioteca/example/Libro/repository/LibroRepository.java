package Biblioteca.example.Libro.repository;

import Biblioteca.example.Libro.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByIsbn(String isbn);
}
