package Biblioteca.example.Multas.client;
import Biblioteca.example.Multas.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-user-multas", url = "${ms.user.url}")
public interface UserClient {
    @GetMapping("/api/users/{id}")
    UserResponseDTO obtenerPorId(@PathVariable Long id);
}
