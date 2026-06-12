package Biblioteca.example.ResenaLibro.repository;
import Biblioteca.example.ResenaLibro.model.ResenaLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ResenaLibroRepository extends JpaRepository<ResenaLibro, Long> {
    List<ResenaLibro> findByLibroId(Long libroId);
}
