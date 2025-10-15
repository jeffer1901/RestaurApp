package com.example.restaurapp.pedido;

import com.example.restaurapp.mesa.Mesa;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;
    private LocalDateTime fechaHora;
    public enum estado {REGISTRADO,EN_PREPARACION,COMPLETADO,CANCELADO};
    private estado estado;
    private Double total;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detallePedidos;
    public Pedido(long id, Mesa mesa, LocalDateTime fechaHora, estado estado, Double total, List<DetallePedido> detallePedidos) {
        this.id = id;
        this.mesa = mesa;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.total = total;
        this.detallePedidos = detallePedidos;
    }
    public Pedido() {}
    public long getId() {
        return id;
    }
    public void setId(long id) {this.id = id;}

    public Mesa getMesa() {return mesa;}

    public void setMesa(Mesa mesa) {this.mesa = mesa;}

    public LocalDateTime getFechaHora() {return fechaHora;}

    public void setFechaHora(LocalDateTime fechaHora) {this.fechaHora = fechaHora;}

    public estado getEstado() {return estado;}

    public void setEstado(estado estado) {this.estado = estado;}

    public Double getTotal() {return total;}

    public void setTotal(Double total) {this.total = total;}

    public List<DetallePedido> getDetallePedidos() {return detallePedidos;}

    public void setDetallePedidos(List<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }
}
