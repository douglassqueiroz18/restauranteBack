package com.restaurante.demo.controller;

import com.restaurante.demo.entity.Kpi;
import com.restaurante.demo.service.KpiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kpis")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class KpiController {

    private final KpiService kpiService;

    @GetMapping
    public ResponseEntity<Kpi> getDashboard() {
        // Retorna o objeto Kpi com total de vendas, ticket médio e contagem por status
        return ResponseEntity.ok(kpiService.gerarRelatorioGeral());
    }
}