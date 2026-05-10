package Biblioteca.example.ReservaLibro.service;
import Biblioteca.example.ReservaLibro.client.LibroClient;
import Biblioteca.example.ReservaLibro.client.MultasClient;
import Biblioteca.example.ReservaLibro.client.UserClient;
import Biblioteca.example.ReservaLibro.dto.EstadoMultasDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroRequestDTO;
import Biblioteca.example.ReservaLibro.dto.ReservaLibroResponseDTO;
import Biblioteca.example.ReservaLibro.model.ReservaLibro;
import Biblioteca.example.ReservaLibro.repository.ReservaLibroRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaLibroService {
    private final ReservaLibroRepository repository;
    private final LibroClient libroClient;
    private final UserClient userClient;
    private final MultasClient multasClient;

    private ReservaLibroResponseDTO mapToDTO(ReservaLibro r, String aviso) {
        return new ReservaLibroResponseDTO(r.getId(), r.getUserId(), r.getLibroId(),
                r.getFechaReserva(), r.getEstadoReserva(), aviso);
    }

    private ReservaLibroResponseDTO mapToDTO(ReservaLibro r) {
        return mapToDTO(r, null);
    }

    private void validarLibro(Long libroId) {
        try { libroClient.obtenerPorId(libroId); }
        catch (FeignException.NotFound ex) {
            log.warn("Libro no encontrado con id: {}", libroId);
            throw new RuntimeException("El libro no existe con id: " + libroId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de libros: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de libros.");
        }
    }

    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) {
            log.warn("Usuario no encontrado con id: {}", userId);
            throw new RuntimeException("El usuario no existe con id: " + userId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de usuarios: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de usuarios.");
        }
    }

    private String validarMultas(Long userId) {
        try {
            EstadoMultasDTO estado = multasClient.obtenerEstado(userId);
            if (!estado.isPuedeReservar()) {
                log.warn("Usuario id {} bloqueado por multas. Total puntos: {}", userId, estado.getTotalCantidad());
                throw new RuntimeException(estado.getAviso());
            }
            if (estado.getTotalMultas() > 0) {
                log.info("Usuario id {} tiene {} multa(s) pero puede reservar", userId, estado.getTotalMultas());
            }
            return estado.getTotalMultas() > 0 ? estado.getAviso() : null;
        } catch (FeignException ex) {
            log.error("Error al contactar servicio de multas: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de multas.");
        }
    }

    public List<ReservaLibroResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las reservas");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<ReservaLibroResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando reserva con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public ReservaLibroResponseDTO guardar(ReservaLibroRequestDTO dto) {
        log.info("Creando reserva para usuario id: {} y libro id: {}", dto.getUserId(), dto.getLibroId());
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        String aviso = validarMultas(dto.getUserId());
        if ("ACTIVA".equals(dto.getEstadoReserva())) {
            repository.findByLibroIdAndEstadoReserva(dto.getLibroId(), "ACTIVA")
                    .ifPresent(r -> {
                        log.warn("Libro id {} ya tiene reserva ACTIVA", dto.getLibroId());
                        throw new RuntimeException("El libro ya tiene una reserva activa.");
                    });
        }
        ReservaLibro r = new ReservaLibro(null, dto.getUserId(), dto.getLibroId(),
                dto.getFechaReserva(), dto.getEstadoReserva());
        ReservaLibro saved = repository.save(r);
        log.info("Reserva creada exitosamente con id: {}", saved.getId());
        return mapToDTO(saved, aviso);
    }

    public ReservaLibroResponseDTO actualizar(Long id, ReservaLibroRequestDTO dto) {
        log.info("Actualizando reserva con id: {}", id);
        ReservaLibro r = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Reserva no encontrada con id: {}", id);
                    return new RuntimeException("Reserva no encontrada con id: " + id);
                });
        validarUser(dto.getUserId());
        validarLibro(dto.getLibroId());
        r.setUserId(dto.getUserId());
        r.setLibroId(dto.getLibroId());
        r.setFechaReserva(dto.getFechaReserva());
        r.setEstadoReserva(dto.getEstadoReserva());
        ReservaLibro updated = repository.save(r);
        log.info("Reserva actualizada exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando reserva con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar reserva inexistente con id: {}", id);
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Reserva eliminada exitosamente con id: {}", id);
    }
}
