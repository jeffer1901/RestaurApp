package com.example.restaurapp.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    @GetMapping("/rol/{rol}")
    public List<Usuario> getAllRol(@PathVariable Usuario.Rol rol){return usuarioService.getByRol(rol);}

    public UsuarioController(UsuarioService usuarioService) {this.usuarioService = usuarioService;}
    @GetMapping("/{id}")
    public Optional<Usuario> getUsuario(@PathVariable Long id) {return usuarioService.getById(id);}

    @GetMapping()
    public List<Usuario> getAll(){return usuarioService.getAll();}

    @PostMapping("/registro")
    public Usuario save(@RequestBody Usuario usuario) {return usuarioService.save(usuario);}
    @PutMapping("/{id}")
    public Usuario update(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.update(id, usuario);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }

}
