package com.example.restaurapp.mesa;

import com.example.restaurapp.pedido.Pedido;
import com.example.restaurapp.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="mesas")
public class Mesa {
    @Id
    private long id;
    private int capacidad;
    public enum Estado{LIBRE,OCUPADA,RESERVADA};
    @Enumerated(EnumType.STRING)
    private Estado estado;
    @ManyToOne
    @JoinColumn(name = "mesero_id")
    private Usuario mesero;
    @OneToMany(mappedBy = "mesa")
    @JsonIgnore
    private List<Pedido> pedidos;

    public  Mesa(int capacidad,long id,Estado estado,Usuario mesero,List<Pedido> pedidos) {
        this.capacidad = capacidad;
        this.id = id;
        this.estado = estado;
        this.mesero = mesero;
        this.pedidos = pedidos;
    }
    public Mesa(){}
    public long getId() {
        return id;
    }
    public void setId(long id) {this.id = id;}
    public int getCapacidad() {return capacidad;}
    public void setCapacidad(int capacidad) {this.capacidad = capacidad;}
    public Estado getEstado() {return estado;}
    public void setEstado(Estado estado) {this.estado = estado;}
    public Usuario getMesero() {return mesero;}
    public void setMesero(Usuario mesero) {this.mesero = mesero;}
}
