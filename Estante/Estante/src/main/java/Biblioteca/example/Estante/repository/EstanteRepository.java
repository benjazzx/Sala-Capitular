package Biblioteca.example.Estante.repository;
import Biblioteca.example.Estante.model.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EstanteRepository extends JpaRepository<Estante, Long> {
    boolean existsByLibroId(Long libroId);
}
