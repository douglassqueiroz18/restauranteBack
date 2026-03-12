package com.restaurante.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

public interface KpiRepository {
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.status != 'CANCELADO'")
    Double sumTotalVendas();

    @Query("SELECT COUNT(p) FROM Pedido p")
    Long countTotalPedidos();

    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
    List<Object[]> countPedidosByStatus();
}
