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
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
                throw new RuntimeException("El usuario con id " + adminId + " no tiene permisos de administrador.");
            }
        } catch (FeignException.NotFound ex) {
            throw new RuntimeException("El administrador no existe con id: " + adminId);
        } catch (FeignException ex) {
            throw new RuntimeException("No se puede contactar con el servicio de usuarios.");
        }
    }
    private void validarUser(Long userId) {
        try { userClient.obtenerPorId(userId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El usuario no existe con id: " + userId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de usuarios."); }
    }
    private void validarHistorial(Long historialId) {
        try { historialClient.obtenerPorId(historialId); }
        catch (FeignException.NotFound ex) { throw new RuntimeException("El historial no existe con id: " + historialId); }
        catch (FeignException ex) { throw new RuntimeException("No se puede contactar con el servicio de historial."); }
    }
    public List<MultaResponseDTO> obtenerTodos() {
        return repository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public Optional<MultaResponseDTO> obtenerPorId(Long id) {
        return repository.findById(id).map(this::mapToDTO);
    }
    public List<MultaResponseDTO> obtenerPorUsuario(Long userId) {
        return repository.findByUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<MultaResponseDTO> obtenerPorTipo(String tipo) {
        return repository.findByTipo(tipo).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public EstadoMultasDTO obtenerEstadoUsuario(Long userId) {
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
        return new EstadoMultasDTO(puede, multas.size(), totalCantidad, aviso.toString());
    }
    public boolean puedeReservar(Long userId) {
        Integer total = repository.sumCantidadByUserId(userId);
        return total <= 3;
    }
    public MultaResponseDTO guardar(MultaRequestDTO dto) {
        validarAdmin(dto.getAdminId());
        validarUser(dto.getUserId());
        validarHistorial(dto.getHistorialId());
        Multa m = new Multa(null, dto.getUserId(), dto.getHistorialId(),
                dto.getDescripcion(), dto.getFecha(), dto.getCantidad(), dto.getAdminId(), dto.getTipo());
        return mapToDTO(repository.save(m));
    }
    public MultaResponseDTO actualizar(Long id, MultaRequestDTO dto) {
        Multa m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con id: " + id));
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
        return mapToDTO(repository.save(m));
    }
    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Multa no encontrada con id: " + id);
        repository.deleteById(id);
    }
}
