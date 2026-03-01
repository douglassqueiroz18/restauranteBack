package com.restaurante.demo.service;

import com.restaurante.demo.entity.Pedido;
import com.restaurante.demo.entity.StatusPedido;
import com.restaurante.demo.repository.PedidoRepository;
import com.restaurante.demo.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    public List<Pedido> listarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status.name());
    }
    
    public List<Pedido> listarPorMesa(Long mesaId) {
        return pedidoRepository.findByMesaId(mesaId);
    }
    
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    public Pedido salvar(Pedido pedido) {
        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> {
                item.setPedido(pedido);
            });
        }
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {

        return pedidoRepository.findById(id)
            .map(pedido -> {
                pedido.setStatus(pedidoAtualizado.getStatus());
                pedido.setTotal(pedidoAtualizado.getTotal());
                pedido.setObservacoes(pedidoAtualizado.getObservacoes());
                pedido.getItens().clear();
                if (pedidoAtualizado.getItens() != null) {
                pedidoAtualizado.getItens().forEach(item -> {
                    item.setPedido(pedido);
                    pedido.getItens().add(item);
                });
            }
                return pedidoRepository.save(pedido);
            })
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
    
    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
    
    public void atualizarStatus(Long id, StatusPedido novoStatus) {
        pedidoRepository.findById(id)
            .ifPresent(pedido -> {
                pedido.setStatus(novoStatus);
                pedidoRepository.save(pedido);
            });
    }
}
