package com.example.restaurapp.pedido;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping()
    public List<Pedido> getAllPedidos(){
        return pedidoService.getAllPedidos();
    }

    @GetMapping("{id}")
    public Optional<Pedido> getPedidoById(@PathVariable Long id){return pedidoService.getPedidoById(id);}

    @PostMapping
    public Pedido addPedido(@RequestBody Pedido pedido){return pedidoService.createPedido(pedido);}

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(
            @PathVariable Long id,
            @RequestBody Pedido pedidoActualizado,
            @RequestParam String rol) {
        Pedido pedido = pedidoService.updatePedido(id, pedidoActualizado, rol);
        return ResponseEntity.ok(pedido);
    }
    @DeleteMapping("/{id}")
    public void deletePedido(@PathVariable Long id){
        pedidoService.deletePedido(id);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        pedidoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok("Estado del pedido actualizado");
    }


}
