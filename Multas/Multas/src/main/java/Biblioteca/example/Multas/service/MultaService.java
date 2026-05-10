package Biblioteca.example.Multas.service;
import Biblioteca.example.Multas.client.HistorialClient;
import Biblioteca.example.Multas.client.UserClient;
import Biblioteca.example.Multas.dto.EstadoMultasDTO;
import Biblioteca.example.Multas.dto.MultaRequestDTO;
import Biblioteca.example.Multas.dto.MultaResponseDTO;
import Biblioteca.example.Multas.dto.UserResponseDTO;
import Biblioteca.example.Multas.model.Multa;
import Biblioteca.example.Multas.repository.MultaRepository;
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
public class MultaService {
    private final MultaRepository repository;
    private final UserClient userClient;
    private final HistorialClient historialClient;

    private MultaResponseDTO mapToDTO(Multa m) {
        return new MultaResponseDTO(m.getId(), m.getAdminId(), m.getUserId(), m.getHistorialId(),
                m.getDescripcion(), m.getFecha(), m.getCantidad(), m.getTipo());
    }

    private void validarAdmin(Long adminId) {
        try {
            UserResponseDTO admin = userClient.obtenerPorId(adminId);
            if (!"ADMIN".equals(admin.getRolNombre())) {
                log.warn("Usuario id {} no tiene rol ADMIN para multar", adminId);
                throw new RuntimeException("El usuario con id " + adminId + " no tiene permisos de administrador.");
            }
        } catch (FeignException.NotFound ex) {
            log.warn("Administrador no encontrado con id: {}", adminId);
            throw new RuntimeException("El administrador no existe con id: " + adminId);
        } catch (FeignException ex) {
            log.error("Error al contactar servicio de usuarios: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de usuarios.");
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

    private void validarHistorial(Long historialId) {
        try { historialClient.obtenerPorId(historialId); }
        catch (FeignException.NotFound ex) {
            log.warn("Historial no encontrado con id: {}", historialId);
            throw new RuntimeException("El historial no existe con id: " + historialId);
        }
        catch (FeignException ex) {
            log.error("Error al contactar servicio de historial: {}", ex.getMessage());
            throw new RuntimeException("No se puede contactar con el servicio de historial.");
        }
    }

    public List<MultaResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las multas");
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<MultaResponseDTO> obtenerPorId(Long id) {
        log.info("Buscando multa con id: {}", id);
        return repository.findById(id).map(this::mapToDTO);
    }

    public List<MultaResponseDTO> obtenerPorUsuario(Long userId) {
        log.info("Obteniendo multas del usuario con id: {}", userId);
        return repository.findByUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<MultaResponseDTO> obtenerPorTipo(String tipo) {
        log.info("Obteniendo multas por tipo: {}", tipo);
        return repository.findByTipo(tipo).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public EstadoMultasDTO obtenerEstadoUsuario(Long userId) {
        log.info("Consultando estado de multas para usuario id: {}", userId);
        List<Multa> multas = repository.findByUserId(userId);
        int totalCantidad = repository.sumCantidadByUserId(userId);
        boolean puede = totalCantidad <= 3;
        if (multas.isEmpty()) {
            return new EstadoMultasDTO(true, 0, 0, "Sin multas pendientes.");
        }
        StringBuilder aviso = new StringBuilder();
        if (!puede) aviso.append("RESERVA BLOQUEADA. ");
        aviso.append("Tiene ").append(multas.size()).append(" multa(s) (total: ").append(totalCantidad).append(" punto(s)): ");
        for (int i = 0; i < multas.size(); i++) {
            Multa m = multas.get(i);
            aviso.append("[").append(i + 1).append("] ")
                 .append(m.getTipo()).append(" - ")
                 .append(m.getDescripcion())
                 .append(" (").append(m.getFecha()).append(")");
            if (i < multas.size() - 1) aviso.append(" | ");
        }
        log.info("Estado multas usuario {}: puedeReservar={}, total={}", userId, puede, totalCantidad);
        return new EstadoMultasDTO(puede, multas.size(), totalCantidad, aviso.toString());
    }

    public boolean puedeReservar(Long userId) {
        Integer total = repository.sumCantidadByUserId(userId);
        log.info("Verificando si usuario {} puede reservar. Total puntos: {}", userId, total);
        return total <= 3;
    }

    public MultaResponseDTO guardar(MultaRequestDTO dto) {
        log.info("Admin id {} multando a usuario id {} por tipo: {}", dto.getAdminId(), dto.getUserId(), dto.getTipo());
        validarAdmin(dto.getAdminId());
        validarUser(dto.getUserId());
        validarHistorial(dto.getHistorialId());
        Multa m = new Multa(null, dto.getUserId(), dto.getHistorialId(),
                dto.getDescripcion(), dto.getFecha(), dto.getCantidad(), dto.getAdminId(), dto.getTipo());
        Multa saved = repository.save(m);
        log.info("Multa registrada exitosamente con id: {}", saved.getId());
        return mapToDTO(saved);
    }

    public MultaResponseDTO actualizar(Long id, MultaRequestDTO dto) {
        log.info("Actualizando multa con id: {}", id);
        Multa m = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Multa no encontrada con id: {}", id);
                    return new RuntimeException("Multa no encontrada con id: " + id);
                });
        validarAdmin(dto.getAdminId());
        validarUser(dto.getUserId());
        validarHistorial(dto.getHistorialId());
        m.setAdminId(dto.getAdminId());
        m.setUserId(dto.getUserId());
        m.setHistorialId(dto.getHistorialId());
        m.setDescripcion(dto.getDescripcion());
        m.setFecha(dto.getFecha());
        m.setCantidad(dto.getCantidad());
        m.setTipo(dto.getTipo());
        Multa updated = repository.save(m);
        log.info("Multa actualizada exitosamente con id: {}", updated.getId());
        return mapToDTO(updated);
    }

    public void eliminar(Long id) {
        log.info("Eliminando multa con id: {}", id);
        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar multa inexistente con id: {}", id);
            throw new RuntimeException("Multa no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Multa eliminada exitosamente con id: {}", id);
    }
}
