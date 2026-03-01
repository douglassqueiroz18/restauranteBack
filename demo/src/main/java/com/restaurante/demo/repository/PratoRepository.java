package com.restaurante.demo.repository;

import com.restaurante.demo.entity.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Long> {
    List<Prato> findByCategoria(String categoria);
    List<Prato> findByAtivoTrue();
    List<Prato> findByAtivoTrueAndCategoria(String categoria);
}
