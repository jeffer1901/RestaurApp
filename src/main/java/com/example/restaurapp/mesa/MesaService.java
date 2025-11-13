package com.example.restaurapp.mesa;

import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;

    public MesaService(MesaRepository mesaRepository, UsuarioRepository usuarioRepository) {
        this.mesaRepository = mesaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 🔹 Obtener todas las mesas
    public List<Mesa> getAll() {
        return mesaRepository.findAll();
    }

    // 🔹 Obtener una mesa por ID
    public Optional<Mesa> getMesaById(Long id) {
        return mesaRepository.findById(id);
    }

    // 🔹 Crear una nueva mesa o actualizar una existente
    public Mesa save(Mesa mesa) {
        if (mesa.getMesero() != null && mesa.getMesero().getId() != 0) {
            Usuario mesero = usuarioRepository.findById(mesa.getMesero().getId())
                    .orElseThrow(() -> new RuntimeException("Mesero no encontrado"));

            if (mesero.getRol() != Usuario.Rol.MESERO) {
                throw new RuntimeException("El usuario asignado no tiene rol de MESERO");
            }

            mesa.setMesero(mesero);
        } else {
            mesa.setMesero(null); // ✅ Permite guardar sin mesero
        }

        return mesaRepository.save(mesa);
    }

    // 🔹 Eliminar mesa por ID
    public void delete(Long id) {
        mesaRepository.deleteById(id);
    }

    // 🔹 Actualizar mesa existente
    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Optional<Mesa> mesaOptional = mesaRepository.findById(id);

        if (mesaOptional.isEmpty()) {
            return null;
        }

        Mesa mesaExistente = mesaOptional.get();
        mesaExistente.setCapacidad(mesaActualizada.getCapacidad());
        mesaExistente.setEstado(mesaActualizada.getEstado());

        if (mesaActualizada.getMesero() != null && mesaActualizada.getMesero().getId() != 0) {
            Usuario mesero = usuarioRepository.findById(mesaActualizada.getMesero().getId())
                    .orElseThrow(() -> new RuntimeException("Mesero no encontrado"));
            mesaExistente.setMesero(mesero);
        } else {
            mesaExistente.setMesero(null); // ✅ Se puede actualizar a "sin mesero"
        }

        return mesaRepository.save(mesaExistente);
    }

    // 🔹 Liberar mesa (cambiar estado a LIBRE y quitar mesero)
    public void liberarMesa(Long idMesa) {
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + idMesa));

        if (mesa.getEstado() == Mesa.Estado.LIBRE) {
            throw new RuntimeException("La mesa ya está libre");
        }

        mesa.setEstado(Mesa.Estado.LIBRE);
        mesa.setMesero(null);
        mesaRepository.save(mesa);
    }
}
