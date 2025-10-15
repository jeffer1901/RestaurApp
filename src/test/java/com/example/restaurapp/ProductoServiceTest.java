package com.example.restaurapp;

import com.example.restaurapp.productos.Producto;
import com.example.restaurapp.productos.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Test
    void testGetAll() {
        List<Producto> productos = productoService.getAll();
        assertTrue(productos.size() >= 5); // basado en tu data.sql
    }

    @Test
    void testSaveProducto() {
        Producto nuevo = new Producto();
        nuevo.setNombre("Pasta");
        nuevo.setDescripcion("Carbonara");
        nuevo.setPrecio(20000);
        nuevo.setTipo(Producto.tipo.PLATO_FUERTE);
        nuevo.setDisponible(true);

        Producto guardado = productoService.save(nuevo);
        assertNotNull(guardado.getId());
        assertEquals("Pasta", guardado.getNombre());
    }

    @Test
    void testUpdateProducto() {
        Producto existente = productoService.getAll().get(0);
        existente.setPrecio(9999);
        Producto actualizado = productoService.update(existente.getId(), existente);
        assertEquals(9999, actualizado.getPrecio());
    }

    @Test
    void testCambiarDisponibilidad() {
        Producto p = productoService.getAll().get(1);
        boolean estadoOriginal = p.isDisponible();
        productoService.cambiarDisponibilidad(p.getId(), !estadoOriginal);
        Optional<Producto> cambiado = productoService.getById(p.getId());
        assertEquals(!estadoOriginal, cambiado.get().isDisponible());
    }
}