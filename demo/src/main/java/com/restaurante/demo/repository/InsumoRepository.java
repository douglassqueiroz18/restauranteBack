package com.restaurante.demo.repository;

import com.restaurante.demo.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    
    // Busca insumos que precisam de reposição urgente
    @Query("SELECT i FROM Insumo i WHERE i.quantidadeAtual <= i.estoqueSeguranca")
    List<Insumo> findItensCriticos();
}