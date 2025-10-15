package com.example.restaurapp.pedido;

import com.example.restaurapp.mesa.Mesa;
import com.example.restaurapp.mesa.MesaRepository;
import com.example.restaurapp.productos.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;
    private MesaRepository mesaRepository;
    private ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> getPedidoById(Long id) {
        return pedidoRepository.findById(id);
    }
    public Pedido createPedido(Pedido pedido) {

        Mesa mesa = mesaRepository.findById(pedido.getMesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));


        pedido.setMesa(mesa);
        pedido.setFechaHora(java.time.LocalDateTime.now());
        pedido.setEstado(Pedido.estado.REGISTRADO);


        double total = 0;

        for (var detalle : pedido.getDetallePedidos()) {
            var producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getId()));

            detalle.setPedido(pedido); // Relación bidireccional
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPrecioTotal(producto.getPrecio() * detalle.getCantidad());

            total += detalle.getPrecioTotal();
        }

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }
    public Pedido updatePedido(Long id, Pedido pedidoActualizado, String rolUsuario) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));

        if (pedidoExistente.getEstado() == Pedido.estado.COMPLETADO ||
                pedidoExistente.getEstado() == Pedido.estado.CANCELADO) {
            throw new RuntimeException("No se puede modificar un pedido entregado o cancelado");
        }

        if (rolUsuario.equalsIgnoreCase("MESERO")) {
            if (pedidoActualizado.getDetallePedidos() != null && !pedidoActualizado.getDetallePedidos().isEmpty()) {
                pedidoExistente.getDetallePedidos().clear();

                double nuevoTotal = 0;

                for (var detalle : pedidoActualizado.getDetallePedidos()) {
                    var producto = productoRepository.findById(detalle.getProducto().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalle.getProducto().getId()));

                    detalle.setPedido(pedidoExistente);
                    detalle.setProducto(producto);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalle.setPrecioTotal(producto.getPrecio() * detalle.getCantidad());

                    nuevoTotal += detalle.getPrecioTotal();
                    pedidoExistente.getDetallePedidos().add(detalle);
                }

                pedidoExistente.setTotal(nuevoTotal);
            } else {
                throw new RuntimeException("El detalle del pedido no puede estar vacío");
            }
        }

        else if (rolUsuario.equalsIgnoreCase("COCINERO")) {
            if (pedidoActualizado.getEstado() != null) {
                pedidoExistente.setEstado(pedidoActualizado.getEstado());
            } else {
                throw new RuntimeException("Debe indicar el nuevo estado del pedido");
            }
        }
        else {
            throw new RuntimeException("Rol no autorizado para modificar el pedido");
        }

        return pedidoRepository.save(pedidoExistente);
    }
    public void cambiarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + idPedido));

        try {
            Pedido.estado estado = Pedido.estado.valueOf(nuevoEstado.toUpperCase());
            pedido.setEstado(estado);
            pedidoRepository.save(pedido);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + nuevoEstado +
                    ". Usa: REGISTRADO, EN_PREPARACION, COMPLETADO o CANCELADO");
        }
    }


}
