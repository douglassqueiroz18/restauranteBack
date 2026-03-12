package com.restaurante.demo.entity;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kpi {
    private Double totalVendas;
    private Long quantidadePedidos;
    private Double ticketMedio;
    private Map<String, Long> pedidosPorStatus;
}
