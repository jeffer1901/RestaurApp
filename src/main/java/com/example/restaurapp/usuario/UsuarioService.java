package com.example.restaurapp.usuario;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    public UsuarioService(UsuarioRepository usuarioRepository){this.usuarioRepository=usuarioRepository;}
    public List<Usuario> getAll(){
        return usuarioRepository.findAll();
    }
    public Optional<Usuario> getById(Long id) {return usuarioRepository.findById(id);}
    public Usuario save(Usuario usuario) {return usuarioRepository.save(usuario);}
    public Usuario update(Long id, Usuario datos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datos.getNombre());
            usuario.setApellido(datos.getApellido());
            usuario.setCorreo(datos.getCorreo());
            usuario.setPassword(datos.getPassword());
            usuario.setRol(datos.getRol());
            return usuarioRepository.save(usuario);
        }).orElse(null);
    }
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
    public List<Usuario> getByRol(Usuario.Rol rol) {return usuarioRepository.findAllByRol(rol);}


}
