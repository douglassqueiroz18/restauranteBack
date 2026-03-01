package com.restaurante.demo.repository;

import com.restaurante.demo.entity.Mesa;
import com.restaurante.demo.entity.StatusMesa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByStatus(StatusMesa status);
}
