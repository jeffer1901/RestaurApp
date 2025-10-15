package com.example.restaurapp.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@RequestMapping
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    Optional<Usuario> findByCorreo(String correo);
    List<Usuario> findByRol(Usuario.Rol rol);

    List<Usuario> findAllByRol(Usuario.Rol rol);
}
