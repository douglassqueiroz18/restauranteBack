package com.restaurante.demo.controller;

import com.restaurante.demo.entity.Pedido;
import com.restaurante.demo.entity.StatusPedido;
import com.restaurante.demo.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pedido>> listarPorStatus(@PathVariable StatusPedido status) {
        return ResponseEntity.ok(pedidoService.listarPorStatus(status));
    }
    
    @GetMapping("/mesa/{mesaId}")
    public ResponseEntity<List<Pedido>> listarPorMesa(@PathVariable Long mesaId) {
        return ResponseEntity.ok(pedidoService.listarPorMesa(mesaId));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        System.out.println("Recebendo pedido: " + pedido);
        Pedido pedidoSalvo = pedidoService.salvar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(
        @PathVariable Long id, 
        @RequestBody Pedido pedidoAtualizado, 
        @RequestParam(name = "deveEstornar") boolean deveEstornar) {
        System.err.println("Atualizando pedido ID " + id + " parametro: " + deveEstornar);
        Pedido pedido = pedidoService.atualizar(id, pedidoAtualizado, deveEstornar);
        return ResponseEntity.ok(pedido);
    }
    
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @PathVariable StatusPedido status) {
        pedidoService.atualizarStatus(id, status);
        return ResponseEntity.ok().build();
    }
    
}
