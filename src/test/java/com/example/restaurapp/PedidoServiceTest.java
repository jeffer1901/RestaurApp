package com.example.restaurapp;

import com.example.restaurapp.mesa.Mesa;
import com.example.restaurapp.mesa.MesaRepository;
import com.example.restaurapp.pedido.DetallePedido;
import com.example.restaurapp.pedido.Pedido;
import com.example.restaurapp.pedido.PedidoRepository;
import com.example.restaurapp.pedido.PedidoService;
import com.example.restaurapp.productos.Producto;
import com.example.restaurapp.productos.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MesaRepository mesaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private Mesa mesa;
    private Producto producto;
    private DetallePedido detalle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mesa = new Mesa();
        mesa.setId(1L);
        mesa.setEstado(Mesa.Estado.OCUPADA);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Pizza");
        producto.setPrecio(18000.0);

        detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(2);

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setMesa(mesa);
        pedido.setDetallePedidos(List.of(detalle));
    }

    @Test
    void testCrearPedido() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(mesa));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        Pedido creado = pedidoService.createPedido(pedido);

        assertNotNull(creado);
        assertEquals(36000.0, creado.getTotal());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testCambiarEstadoPedido() {
        pedido.setEstado(Pedido.estado.REGISTRADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cambiarEstado(1L, "COMPLETADO");
        assertEquals(Pedido.estado.COMPLETADO, pedido.getEstado());
    }

    @Test
    void testEliminarPedido() {
        pedidoService.deletePedido(1L);
        verify(pedidoRepository, times(1)).deleteById(1L);
    }
}
