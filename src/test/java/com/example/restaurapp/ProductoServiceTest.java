package com.example.restaurapp;

import com.example.restaurapp.productos.Producto;
import com.example.restaurapp.productos.ProductoRepository;
import com.example.restaurapp.productos.ProductoService;
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
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pizza");
        producto.setDescripcion("Hawaiana");
        producto.setPrecio(18000);
        producto.setTipo(Producto.tipo.valueOf("PLATO_FUERTE"));
        producto.setDisponible(true);
    }

    @Test
    void testGetAll() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        assertEquals(1, productoService.getAll().size());
    }

    @Test
    void testSaveProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        Producto guardado = productoService.save(producto);
        assertEquals("Pizza", guardado.getNombre());
    }

    @Test
    void testUpdateProducto() {
        Producto actualizado = new Producto();
        actualizado.setNombre("Limonada");
        actualizado.setDescripcion("Natural");
        actualizado.setPrecio(5000);
        actualizado.setTipo(Producto.tipo.valueOf("BEBIDA"));
        actualizado.setDisponible(true);

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(actualizado);

        Producto result = productoService.update(1L, actualizado);
        assertEquals("Limonada", result.getNombre());
    }

    @Test
    void testCambiarDisponibilidad() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.cambiarDisponibilidad(1L, false);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
}
