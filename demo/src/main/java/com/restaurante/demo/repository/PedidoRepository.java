package com.restaurante.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.restaurante.demo.entity.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatus(String status);
    List<Pedido> findByMesaId(Long mesaId);
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens WHERE p.id = :id")
    Optional<Pedido> findByIdComItens(@Param("id") Long id);
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.status != 'CANCELADO'")
    Double sumTotalVendas();

    // Conta o total de pedidos no sistema
    @Query("SELECT COUNT(p) FROM Pedido p")
    Long countTotalPedidos();

    // Retorna uma lista de arrays: [ "PENDENTE", 5 ], [ "ENTREGUE", 10 ], etc.
    @Query("SELECT p.status, COUNT(p) FROM Pedido p GROUP BY p.status")
    List<Object[]> countPedidosByStatus();
}
