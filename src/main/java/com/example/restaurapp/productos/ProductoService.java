package com.example.restaurapp.productos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private ProductoRepository productoRepository;
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> getById(Long id) {
        return productoRepository.findById(id);
    }
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }
    public Producto cambiarDisponibilidad(Long id, boolean disponible) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setDisponible(disponible);
            return productoRepository.save(producto);
        }
        return null;
    }
    public Producto update(Long id, Producto productoActualizado) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setDescripcion(productoActualizado.getDescripcion());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setTipo(productoActualizado.getTipo());
            producto.setDisponible(productoActualizado.isDisponible());
            return productoRepository.save(producto);
        }
        return null;
    }
}
