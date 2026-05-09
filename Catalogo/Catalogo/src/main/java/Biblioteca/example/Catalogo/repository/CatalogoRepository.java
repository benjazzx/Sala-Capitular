package Biblioteca.example.Catalogo.repository;

import Biblioteca.example.Catalogo.model.Catalogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
}
