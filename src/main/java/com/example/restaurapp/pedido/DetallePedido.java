package com.example.restaurapp.pedido;

import com.example.restaurapp.productos.Producto;
import jakarta.persistence.*;

@Entity
@Table(name="pedidos_detallados")
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer cantidad;
    private double precioUnitario;
    private double precioTotal;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    public DetallePedido(long id, Integer cantidad, double precioUnitario, double precioTotal, Pedido pedido, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.pedido = pedido;
        this.producto = producto;
    }
    public DetallePedido() {}
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}
    public double getPrecioUnitario() {return precioUnitario;}
    public double setPrecioUnitario(double precioUnitario) {return this.precioUnitario = precioUnitario;}
    public double getPrecioTotal() {return precioTotal;}
    public double setPrecioTotal(double precioTotal) {return this.precioTotal = precioTotal;}
    public Pedido getPedido() {return pedido;}
    public void setPedido(Pedido pedido) {this.pedido = pedido;}
    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}


}
