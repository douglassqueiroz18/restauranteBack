package com.restaurante.demo.service;

import com.restaurante.demo.entity.Prato;
import com.restaurante.demo.repository.PratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PratoService {
    
    private final PratoRepository pratoRepository;
    
    public List<Prato> listarTodos() {
        return pratoRepository.findAll();
    }
    
    public List<Prato> listarAtivos() {
        return pratoRepository.findByAtivoTrue();
    }
    
    public List<Prato> listarPorCategoria(String categoria) {
        return pratoRepository.findByCategoria(categoria);
    }
    
    public List<Prato> listarAtivosPorCategoria(String categoria) {
        return pratoRepository.findByAtivoTrueAndCategoria(categoria);
    }
    
    public Optional<Prato> buscarPorId(Long id) {
        return pratoRepository.findById(id);
    }
    
    public Prato salvar(Prato prato) {
        return pratoRepository.save(prato);
    }
    
    public Prato atualizar(Long id, Prato pratoAtualizado) {
        return pratoRepository.findById(id)
            .map(prato -> {
                prato.setNome(pratoAtualizado.getNome());
                prato.setDescricao(pratoAtualizado.getDescricao());
                prato.setPreco(pratoAtualizado.getPreco());
                prato.setEstoque(pratoAtualizado.getEstoque());
                prato.setCategoria(pratoAtualizado.getCategoria());
                prato.setAtivo(pratoAtualizado.getAtivo());
                return pratoRepository.save(prato);
            })
            .orElseThrow(() -> new RuntimeException("Prato não encontrado"));
    }
    
    public void deletar(Long id) {
        pratoRepository.deleteById(id);
    }
}
