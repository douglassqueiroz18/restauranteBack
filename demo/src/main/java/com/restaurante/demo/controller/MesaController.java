package com.restaurante.demo.controller;

import com.restaurante.demo.entity.Mesa;
import com.restaurante.demo.entity.StatusMesa;
import com.restaurante.demo.service.MesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MesaController {
    
    private final MesaService mesaService;
    
    @GetMapping
    public ResponseEntity<List<Mesa>> listarTodas() {
        return ResponseEntity.ok(mesaService.listarTodas());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Mesa>> listarPorStatus(@PathVariable StatusMesa status) {
        return ResponseEntity.ok(mesaService.listarPorStatus(status));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> buscarPorId(@PathVariable Long id) {
        return mesaService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Mesa> criar(@RequestBody Mesa mesa) {
        Mesa mesaSalva = mesaService.salvar(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaSalva);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Mesa> atualizar(@PathVariable Long id, @RequestBody Mesa mesaAtualizada) {
        Mesa mesa = mesaService.atualizar(id, mesaAtualizada);
        return ResponseEntity.ok(mesa);
    }
    
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @PathVariable StatusMesa status) {
        mesaService.atualizarStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        mesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
