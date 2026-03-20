package com.restaurante.demo.service;

import com.restaurante.demo.entity.Prato;
import com.restaurante.demo.repository.PratoRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

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
        if(pratoRepository.existsByNome(prato.getNome())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ja existe um prato cadastrado com o nome: "+ prato.getNome());
        }
        if (prato.getIngredientes() != null) {
            prato.getIngredientes().forEach(item -> item.setPrato(prato));
        }
        Prato salvo = pratoRepository.save(prato);
        return pratoRepository.findById(salvo.getId()).get();    
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
                prato.setFotoUrl(pratoAtualizado.getFotoUrl());
                if (pratoAtualizado.getIngredientes() != null) {
                    prato.getIngredientes().clear();
                    pratoAtualizado.getIngredientes().forEach(novoItem -> {
                        novoItem.setPrato(prato);
                        prato.getIngredientes().add(novoItem);
                    });
                }
                return pratoRepository.save(prato);
            })
            .orElseThrow(() -> new RuntimeException("Prato não encontrado"));
    }
    
    public void deletar(Long id) {
        Prato prato = pratoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));

        if (pratoRepository.countPedidosAtivosComEstePrato(id) > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                "Este prato não pode ser excluído pois está em um pedido sendo preparado ou entregue.");
        }
        boolean temHistorico = pratoRepository.existsVinculoComItensPedido(id);
        if (temHistorico) {
        prato.setAtivo(false);
        pratoRepository.save(prato);
        System.out.println("Prato " + id + " desativado (Soft Delete) com sucesso.");
        } else {
        try {
            pratoRepository.delete(prato);
            pratoRepository.flush();
            System.out.println("Prato " + id + " excluído permanentemente.");
        } catch (DataIntegrityViolationException e) {
            prato.setAtivo(false);
            pratoRepository.save(prato);
        }
    }
    }
}
