package com.restaurante.demo.controller;

import com.restaurante.demo.entity.Insumo;
import com.restaurante.demo.service.InsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class InsumoController {

    private final InsumoService service;

    @GetMapping
    public List<Insumo> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public Insumo criar(@RequestBody Insumo insumo) {
        return service.salvar(insumo);
    }

    @GetMapping("/criticos")
    public List<Insumo> listarCriticos() {
        return service.buscarCriticos();
    }

    @PatchMapping("/{id}/estoque")
    public ResponseEntity<Insumo> ajustarEstoque(@PathVariable Long id, @RequestParam Double quantidade) {
        return ResponseEntity.ok(service.atualizarEstoque(id, quantidade));
    }
}