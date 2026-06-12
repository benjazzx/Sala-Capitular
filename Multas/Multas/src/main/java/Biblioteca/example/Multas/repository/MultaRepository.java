package Biblioteca.example.Multas.repository;
import Biblioteca.example.Multas.model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
public interface MultaRepository extends JpaRepository<Multa, Long> {
    List<Multa> findByUserId(Long userId);
    List<Multa> findByTipo(String tipo);
    @Query("SELECT COALESCE(SUM(m.cantidad), 0) FROM Multa m WHERE m.userId = :userId")
    Integer sumCantidadByUserId(@Param("userId") Long userId);
}
