package com.example.restaurapp.mesa;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mesas")
public class MesaController {
    private MesaService mesaService;
    public MesaController(MesaService mesaService) {this.mesaService = mesaService;}
    @GetMapping("/get/{id}")
    public Optional<Mesa> getMesa(@PathVariable Long id) {return mesaService.getMesaById(id);}

    @GetMapping("/get")
    public List<Mesa> getMesas() {return mesaService.getAll();}

    @PostMapping
    public Mesa save(@RequestBody Mesa mesa) {return mesaService.save(mesa);}
    @PutMapping("/{id}")
    public Mesa update(@PathVariable Long id, @RequestBody Mesa mesa) {return mesaService.actualizarMesa(id, mesa);}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        mesaService.delete(id);
    }
    @PutMapping("/liberar/{id}")
    public ResponseEntity<String> liberarMesa(@PathVariable Long id) {
        mesaService.liberarMesa(id);
        return ResponseEntity.ok("Mesa liberada con éxito");
    }

}
