package com.restaurante.demo.service;

import com.restaurante.demo.entity.Insumo;
import com.restaurante.demo.entity.ItensPedido;
import com.restaurante.demo.entity.Pedido;
import com.restaurante.demo.entity.StatusPedido;
import com.restaurante.demo.repository.PedidoRepository;
import com.restaurante.demo.repository.InsumoRepository;
import com.restaurante.demo.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final InsumoRepository insumoRepository; // Injetar o repositório de insumos
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
    private void baixarEstoqueDosIngredientes(ItensPedido item) {
    if (item.getPrato() != null && item.getPrato().getIngredientes() != null) {
        item.getPrato().getIngredientes().forEach(ingrediente -> {
            Insumo insumo = ingrediente.getInsumo();
            
            Double qtdNecessaria = ingrediente.getQuantidadeNecessaria();
            
            String unidadeFicha = ingrediente.getUnidadeMedida();
            String unidadeEstoque = insumo.getUnidadeMedida(); 

            Double qtdConvertida = converterParaUnidadeEstoque(qtdNecessaria, unidadeFicha, unidadeEstoque);

            Double quantidadeTotalGasta = qtdConvertida * item.getQuantidade();
            Double novoEstoque = insumo.getQuantidadeAtual() - quantidadeTotalGasta;

            if (novoEstoque < 0) {
                throw new RuntimeException("Estoque insuficiente para o insumo: " + insumo.getNome());
            }

            insumo.setQuantidadeAtual(novoEstoque);
            insumoRepository.save(insumo);
        });
    }
}

    private Double converterParaUnidadeEstoque(Double valor, String de, String para) {
        if (de == null || para == null || de.equalsIgnoreCase(para)) {
            return valor;
        }

        if (de.equalsIgnoreCase("G") && para.equalsIgnoreCase("KG")) {
            return valor / 1000.0;
        }

        if (de.equalsIgnoreCase("ML") && para.equalsIgnoreCase("L")) {
            return valor / 1000.0;
        }

        if (de.equalsIgnoreCase("KG") && para.equalsIgnoreCase("G")) {
            return valor * 1000.0;
        }
        
        if (de.equalsIgnoreCase("L") && para.equalsIgnoreCase("ML")) {
            return valor * 1000.0;
        }

        return valor;
    }
    private void estornarEstoqueDosIngredientes(ItensPedido item) {
        System.out.println("Entrou no método estornar para o prato: " + item.getPrato().getNome());
        if (item.getPrato() != null && item.getPrato().getIngredientes() != null) {        
            item.getPrato().getIngredientes().forEach(ingrediente -> {
            Insumo insumo = ingrediente.getInsumo();
            Double qtdConvertida = converterParaUnidadeEstoque(
                ingrediente.getQuantidadeNecessaria(), 
                ingrediente.getUnidadeMedida(), 
                insumo.getUnidadeMedida()
            );

            Double quantidadeRetornada = qtdConvertida * item.getQuantidade();
            System.out.println("DEBUG ESTORNO: Insumo " + insumo.getNome() + " tinha " + insumo.getQuantidadeAtual());
            insumo.setQuantidadeAtual(insumo.getQuantidadeAtual() + quantidadeRetornada);
            System.out.println("DEBUG ESTORNO: Insumo " + insumo.getNome() + " agora tem " + insumo.getQuantidadeAtual());
            insumoRepository.save(insumo);
        });
    }
}
    public Pedido salvar(Pedido pedido) {
        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> {
                item.setPedido(pedido);
                baixarEstoqueDosIngredientes(item);

            });
        }
        
        return pedidoRepository.save(pedido);
    }
    
    
    public Pedido atualizar(Long id, Pedido pedidoAtualizado, boolean deveRetornar) {
    return pedidoRepository.findByIdComItens(id)
        .map(pedido -> {
            System.out.println("Ainda fora no if do deveRetornar: "+ deveRetornar);
            StatusPedido statusAntigo = pedido.getStatus();
            StatusPedido statusNovo = pedidoAtualizado.getStatus();
            if (deveRetornar && statusAntigo != StatusPedido.CANCELADO) {
                List<ItensPedido> itensParaEstornar = new ArrayList<>(pedido.getItens());
                itensParaEstornar.forEach(this::estornarEstoqueDosIngredientes);
            }
            System.out.println("Depois do if do deveRetornar: "+ deveRetornar);

            pedido.setStatus(pedidoAtualizado.getStatus());
            pedido.setTotal(pedidoAtualizado.getTotal());
            pedido.setObservacoes(pedidoAtualizado.getObservacoes());

            pedido.getItens().clear();
            if (pedidoAtualizado.getItens() != null) {
                pedidoAtualizado.getItens().forEach(item -> {
                    item.setPedido(pedido);
                    
                    if (statusNovo != StatusPedido.CANCELADO) {
                        baixarEstoqueDosIngredientes(item); 
                    }
                    System.out.println("ultima parte do metodo: " + item);              
                    pedido.getItens().add(item);
                });
            }
            
            return pedidoRepository.save(pedido);
        })
        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
    
    /*public void deletar(Long id) {
        pedidoRepository.findById(id).ifPresent(pedido -> {
            pedido.getItens().forEach(this::estornarEstoqueDosIngredientes);
            pedidoRepository.delete(pedido);
        });    
    }*/
    
    public void atualizarStatus(Long id, StatusPedido novoStatus) {
        pedidoRepository.findById(id)
            .ifPresent(pedido -> {
                pedido.setStatus(novoStatus);
                pedidoRepository.save(pedido);
            });
    }
}
