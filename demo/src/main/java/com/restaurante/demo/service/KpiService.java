package com.restaurante.demo.service;

import com.restaurante.demo.entity.Kpi;
import com.restaurante.demo.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KpiService {

    private final PedidoRepository pedidoRepository;

    public Kpi gerarRelatorioGeral() {
        Double totalVendas = pedidoRepository.sumTotalVendas();
        Long totalPedidos = pedidoRepository.countTotalPedidos();
        
        // Evita divisão por zero
        Double ticketMedio = (totalPedidos > 0 && totalVendas != null) 
                             ? totalVendas / totalPedidos 
                             : 0.0;

        // Mapeia o status (lista de arrays do banco) para um Map amigável ao Frontend
        Map<String, Long> statusMap = new HashMap<>();
        List<Object[]> resultadosStatus = pedidoRepository.countPedidosByStatus();
        
        for (Object[] row : resultadosStatus) {
            statusMap.put(row[0].toString(), (Long) row[1]);
        }

        return new Kpi(
            totalVendas != null ? totalVendas : 0.0,
            totalPedidos,
            ticketMedio,
            statusMap
        );
    }
}