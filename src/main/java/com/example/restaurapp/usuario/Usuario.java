package com.example.restaurapp.usuario;

import jakarta.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    public enum Rol {ADMIN,MESERO,COCINERO}
    @Enumerated(EnumType.STRING)
    private Rol rol;

    public Usuario(long id, String nombre, String apellido, String correo, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }
    public Usuario() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getCorreo() {return correo;}

    public void setCorreo(String email) {this.correo = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public Rol getRol() {return rol;}

    public void setRol(Rol rol) {this.rol = rol;}



}
