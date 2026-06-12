package Biblioteca.example.Historial.client;
import Biblioteca.example.Historial.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-user-historial", url = "${ms.user.url}")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponseDTO obtenerPorId(@PathVariable Long id);
}
