package Biblioteca.example.ReservaLibro.client;
import Biblioteca.example.ReservaLibro.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-user-reserva", url = "${ms.user.url}")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponseDTO obtenerPorId(@PathVariable Long id);
}
