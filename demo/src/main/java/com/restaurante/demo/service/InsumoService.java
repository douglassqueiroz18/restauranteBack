package com.restaurante.demo.service;

import com.restaurante.demo.entity.Insumo;
import com.restaurante.demo.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoService {

    private final InsumoRepository repository;

    public List<Insumo> listarTodos() {
        return repository.findAll();
    }

    public Insumo salvar(Insumo insumo) {
        return repository.save(insumo);
    }

    public List<Insumo> buscarCriticos() {
        return repository.findItensCriticos();
    }

    @Transactional
    public Insumo atualizarEstoque(Long id, Double quantidade) {
        Insumo insumo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));
        
        double novoEstoque = insumo.getQuantidadeAtual() + quantidade;
        
        if (novoEstoque < 0) {
            throw new RuntimeException("Estoque insuficiente para essa operação!");
        }
        
        insumo.setQuantidadeAtual(novoEstoque);
        return repository.save(insumo);
    }
}