package com.example.restaurapp;

import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import com.example.restaurapp.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Carlos");
        usuario.setApellido("Gómez");
        usuario.setCorreo("carlos@test.com");
        usuario.setPassword("1234");
        usuario.setRol(Usuario.Rol.ADMIN);
    }

    @Test
    void testGetAll() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        assertEquals(1, usuarioService.getAll().size());
    }

    @Test
    void testSaveUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        Usuario guardado = usuarioService.save(usuario);
        assertEquals("Carlos", guardado.getNombre());
    }

    @Test
    void testUpdateUsuario() {
        Usuario actualizado = new Usuario();
        actualizado.setNombre("Ana");
        actualizado.setApellido("Perez");
        actualizado.setCorreo("ana@test.com");
        actualizado.setPassword("abcd");
        actualizado.setRol(Usuario.Rol.MESERO);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);

        Usuario resultado = usuarioService.update(1L, actualizado);
        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    void testDeleteUsuario() {
        usuarioService.delete(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
