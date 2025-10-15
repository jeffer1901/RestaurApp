package com.example.restaurapp;

import com.example.restaurapp.mesa.Mesa;
import com.example.restaurapp.mesa.MesaService;
import com.example.restaurapp.pedido.DetallePedido;
import com.example.restaurapp.pedido.Pedido;
import com.example.restaurapp.pedido.PedidoService;
import com.example.restaurapp.productos.Producto;
import com.example.restaurapp.productos.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PedidoServiceTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private ProductoService productoService;

    @Test
    void testCrearPedido() {
        Mesa mesa = mesaService.getAll().get(0);
        Producto producto = productoService.getAll().get(0);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(2);

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setDetallePedidos(List.of(detalle));

        Pedido creado = pedidoService.createPedido(pedido);

        assertNotNull(creado);
        assertTrue(creado.getTotal() > 0);
    }

    @Test
    void testCambiarEstadoPedido() {
        List<Pedido> pedidos = pedidoService.getAllPedidos();
        assertFalse(pedidos.isEmpty(), "La lista de pedidos no debe estar vacía");

        Pedido pedido = pedidos.get(0);
        pedidoService.cambiarEstado(pedido.getId(), "COMPLETADO");
        Optional<Pedido> actualizado = pedidoService.getPedidoById(pedido.getId());
        assertEquals(Pedido.estado.COMPLETADO, actualizado.get().getEstado());
    }
}