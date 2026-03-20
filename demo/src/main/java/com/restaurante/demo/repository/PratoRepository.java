package com.restaurante.demo.repository;

import com.restaurante.demo.entity.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Long> {
    List<Prato> findByCategoria(String categoria);
    List<Prato> findByAtivoTrue();
    List<Prato> findByAtivoTrueAndCategoria(String categoria);
    @Query("SELECT p FROM Prato p WHERE p.ativo = true AND NOT EXISTS (" +
       "SELECT pi FROM PratoInsumo pi WHERE pi.prato = p AND pi.insumo.quantidadeAtual < pi.quantidadeNecessaria)")
    List<Prato> findDisponiveisNoEstoque();
    boolean existsByNome(String nome);
    Optional<Prato> findByNome(String nome);
    @Query("SELECT COUNT(i) FROM ItensPedido i " +
           "WHERE i.prato.id = :id " +
           "AND i.pedido.status NOT IN ('CANCELADO', 'ENTREGUE')")
    long countPedidosAtivosComEstePrato(@Param("id") Long id);
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM ItensPedido i WHERE i.prato.id = :id")
    boolean existsVinculoComItensPedido(@Param("id") Long id);
}
