package com.example.restaurapp.productos;

import com.example.restaurapp.mesa.MesaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private ProductoService productoService;
    public ProductoController(ProductoService productoService) {this.productoService = productoService;}

    @GetMapping
    public List<Producto> getAll() {
        return productoService.getAll();
    }
    @GetMapping("/{id}")
    public Optional<Producto> getById(@PathVariable Long id) {
        return productoService.getById(id);
    }
    @PostMapping
    public Producto save(@RequestBody Producto producto) {
        return productoService.save(producto);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productoService.delete(id);
    }
    @PatchMapping("/{id}/disponibilidad")
    public Producto cambiarDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        return productoService.cambiarDisponibilidad(id, disponible);
    }
    @PutMapping("/{id}")
    public Producto update(@PathVariable Long id, @RequestBody Producto productoActualizado) {
        return productoService.update(id, productoActualizado);
    }
}
