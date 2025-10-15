package com.example.restaurapp.productos;

import jakarta.persistence.*;

@Entity
@Table(name="productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private boolean disponible=true;
    public enum tipo{BEBIDA,PLATO_FUERTE,ENTRADA,POSTRE}
    @Enumerated(EnumType.STRING)
    private tipo tipo;
    public Producto(long id, String nombre, String descripcion, double precio, boolean disponible, tipo tipo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
        this.tipo= tipo;
    }
    public Producto() {}
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public double getPrecio() {return precio;}
    public void setPrecio(double precio) {this.precio = precio;}
    public boolean isDisponible() {return disponible;}
    public void setDisponible(boolean disponible) {this.disponible = disponible;}
    public tipo getTipo() {return tipo;}
    public void setTipo(tipo tipo) {this.tipo = tipo;}


}
