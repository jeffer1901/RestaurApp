package com.example.restaurapp;

import com.example.restaurapp.mesa.Mesa;
import com.example.restaurapp.mesa.MesaService;
import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MesaServiceTest {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testGetAllMesas() {
        List<Mesa> mesas = mesaService.getAll();
        assertTrue(mesas.size() >= 5);
    }

    @Test
    void testGuardarMesaConMeseroValido() {
        Usuario mesero = usuarioService.getAll().stream()
                .filter(u -> u.getRol() == Usuario.Rol.MESERO)
                .findFirst()
                .orElseThrow();

        Mesa nueva = new Mesa();
        nueva.setEstado(Mesa.Estado.LIBRE);
        nueva.setMesero(mesero);

        Mesa guardada = mesaService.save(nueva);
        assertNotNull(guardada.getId());
        assertEquals(Usuario.Rol.MESERO, guardada.getMesero().getRol());
    }

    @Test
    void testLiberarMesa() {
        Mesa mesa = mesaService.getAll().get(0);
        mesa.setEstado(Mesa.Estado.OCUPADA);
        mesaService.save(mesa);

        mesaService.liberarMesa(mesa.getId());
        Optional<Mesa> liberada = mesaService.getMesaById(mesa.getId());

        assertEquals(Mesa.Estado.LIBRE, liberada.get().getEstado());
    }
}
