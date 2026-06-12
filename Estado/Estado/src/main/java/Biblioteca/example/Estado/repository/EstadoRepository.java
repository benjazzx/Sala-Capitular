package Biblioteca.example.Estado.repository;

import Biblioteca.example.Estado.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    boolean existsByNombre(String nombre);
}
