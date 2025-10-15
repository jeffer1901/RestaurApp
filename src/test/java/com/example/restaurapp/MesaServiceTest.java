package com.example.restaurapp;

import com.example.restaurapp.mesa.Mesa;
import com.example.restaurapp.mesa.MesaRepository;
import com.example.restaurapp.mesa.MesaService;
import com.example.restaurapp.usuario.Usuario;
import com.example.restaurapp.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MesaServiceTest {

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MesaService mesaService;

    private Mesa mesa;
    private Usuario mesero;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mesero = new Usuario();
        mesero.setId(2L);
        mesero.setNombre("Ana");
        mesero.setRol(Usuario.Rol.MESERO);

        mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(Mesa.Estado.LIBRE);
        mesa.setMesero(mesero);
    }

    @Test
    void testGetAllMesas() {
        when(mesaRepository.findAll()).thenReturn(List.of(mesa));
        assertEquals(1, mesaService.getAll().size());
    }

    @Test
    void testGuardarMesaConMeseroValido() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(mesero));
        when(mesaRepository.save(any(Mesa.class))).thenReturn(mesa);

        Mesa guardada = mesaService.save(mesa);
        assertNotNull(guardada);
        assertEquals(Usuario.Rol.MESERO, guardada.getMesero().getRol());
    }

    @Test
    void testLiberarMesa() {
        mesa.setEstado(Mesa.Estado.OCUPADA);
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));

        mesaService.liberarMesa(1L);
        assertEquals(Mesa.Estado.LIBRE, mesa.getEstado());
        assertNull(mesa.getMesero());
    }
}
