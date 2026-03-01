package com.restaurante.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.restaurante.demo.entity.Categoria;

public interface CategoriaRepositry extends JpaRepository<Categoria, Long> {
    
}
