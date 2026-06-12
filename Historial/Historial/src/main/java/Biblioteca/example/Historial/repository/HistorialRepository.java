package Biblioteca.example.Historial.repository;
import Biblioteca.example.Historial.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
public interface HistorialRepository extends JpaRepository<Historial, Long> {
}
