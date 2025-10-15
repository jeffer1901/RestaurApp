package com.example.restaurapp;

import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import com.example.restaurapp.usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void testGetAll() {
        List<Usuario> usuarios = usuarioService.getAll();
        assertTrue(usuarios.size() >= 5);
    }

    @Test
    void testSaveUsuario() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Mario");
        nuevo.setApellido("López");
        nuevo.setCorreo("mario@test.com");
        nuevo.setPassword("1234");
        nuevo.setRol(Usuario.Rol.MESERO);

        Usuario guardado = usuarioService.save(nuevo);
        assertNotNull(guardado.getId());
        assertEquals("Mario", guardado.getNombre());
    }

    @Test
    void testUpdateUsuario() {
        Usuario usuario = usuarioService.getAll().get(0);
        usuario.setNombre("Actualizado");
        Usuario actualizado = usuarioService.update(usuario.getId(), usuario);
        assertEquals("Actualizado", actualizado.getNombre());
    }
    @Test
    void testDeleteUsuario() {
        // Guardamos un nuevo usuario temporal
        Usuario nuevo = new Usuario();
        nuevo.setNombre("Eliminar");
        nuevo.setApellido("Prueba");
        nuevo.setCorreo("delete@test.com");
        nuevo.setPassword("abcd");
        nuevo.setRol(Usuario.Rol.MESERO);

        Usuario guardado = usuarioService.save(nuevo);
        Long id = guardado.getId();

        // Eliminamos al usuario
        usuarioService.delete(id);

        // Verificamos que ya no exista
        List<Usuario> usuarios = usuarioService.getAll();
        boolean existe = usuarios.stream().anyMatch(u -> u.getId() == id);
        assertFalse(existe, "El usuario eliminado no debe existir en la lista");
    }
}