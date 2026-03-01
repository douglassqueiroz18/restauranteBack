package com.restaurante.demo.controller;

import com.restaurante.demo.entity.Prato;
import com.restaurante.demo.service.PratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PratoController {
    
    private final PratoService pratoService;
    
    @GetMapping
    public ResponseEntity<List<Prato>> listarTodos() {
        return ResponseEntity.ok(pratoService.listarTodos());
    }
    
    @GetMapping("/ativos")
    public ResponseEntity<List<Prato>> listarAtivos() {
        return ResponseEntity.ok(pratoService.listarAtivos());
    }
    
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Prato>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(pratoService.listarPorCategoria(categoria));
    }
    
    @GetMapping("/ativos/categoria/{categoria}")
    public ResponseEntity<List<Prato>> listarAtivosPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(pratoService.listarAtivosPorCategoria(categoria));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Prato> buscarPorId(@PathVariable Long id) {
        return pratoService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Prato> criar(@RequestBody Prato prato) {
        Prato pratoSalvo = pratoService.salvar(prato);
        return ResponseEntity.status(HttpStatus.CREATED).body(pratoSalvo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Prato> atualizar(@PathVariable Long id, @RequestBody Prato pratoAtualizado) {
        Prato prato = pratoService.atualizar(id, pratoAtualizado);
        return ResponseEntity.ok(prato);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pratoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
