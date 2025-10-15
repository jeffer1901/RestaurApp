package com.example.restaurapp.mesa;

import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {
    private MesaRepository mesaRepository;
    private UsuarioRepository usuarioRepository;
    public MesaService(MesaRepository mesaRepository,UsuarioRepository usuarioRepository) {this.mesaRepository = mesaRepository;
    this.usuarioRepository = usuarioRepository;}

    public List<Mesa> getAll() {return mesaRepository.findAll();}
    public Optional<Mesa> getMesaById(Long id) {return mesaRepository.findById(id);}
    public Mesa save(Mesa mesa) {
        if (mesa.getMesero() != null) {
            Usuario mesero = usuarioRepository.findById(mesa.getMesero().getId())
                    .orElseThrow(() -> new RuntimeException("Mesero no encontrado"));

            if (mesero.getRol() != Usuario.Rol.MESERO) {
                throw new RuntimeException("El usuario asignado no tiene rol de MESERO");
            }

            mesa.setMesero(mesero);
        }
        return mesaRepository.save(mesa);
    }
    public void delete(Long id) {
        mesaRepository.deleteById(id);
    }
    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Optional<Mesa> mesaOptional = mesaRepository.findById(id);

        if (mesaOptional.isEmpty()) {
            return null;
        }

        Mesa mesaExistente = mesaOptional.get();
        mesaExistente.setEstado(mesaActualizada.getEstado());
        mesaExistente.setMesero(mesaActualizada.getMesero());

        return mesaRepository.save(mesaExistente);
    }
    public void liberarMesa(Long idMesa) {
        Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada con ID: " + idMesa));

        if (mesa.getEstado() == Mesa.Estado.LIBRE) {
            throw new RuntimeException("La mesa ya está libre");
        }

        mesa.setEstado(Mesa.Estado.LIBRE);
        mesa.setMesero(null); // 🔹 se desasigna el mesero

        mesaRepository.save(mesa);
    }

}
