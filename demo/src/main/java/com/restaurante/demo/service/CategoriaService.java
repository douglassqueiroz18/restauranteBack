package com.restaurante.demo.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.restaurante.demo.entity.Categoria;
import com.restaurante.demo.repository.CategoriaRepositry;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepositry categoriaRepositry;

    public List<Categoria> listarTodas() {
        return categoriaRepositry.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepositry.findById(id);
    }

    public Categoria salvar(Categoria categoria) {
        return categoriaRepositry.save(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        return categoriaRepositry.findById(id)
            .map(categoria -> {
                categoria.setNome(categoriaAtualizada.getNome());
                return categoriaRepositry.save(categoria);
            })
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public void deletar(Long id) {
        categoriaRepositry.deleteById(id);
    }   
}