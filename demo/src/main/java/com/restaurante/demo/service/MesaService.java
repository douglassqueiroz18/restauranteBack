package com.restaurante.demo.service;

import com.restaurante.demo.entity.Mesa;
import com.restaurante.demo.entity.StatusMesa;
import com.restaurante.demo.repository.MesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MesaService {
    
    private final MesaRepository mesaRepository;
    
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }
    
    public List<Mesa> listarPorStatus(StatusMesa status) {
        return mesaRepository.findByStatus(status);
    }
    
    public Optional<Mesa> buscarPorId(Long id) {
        return mesaRepository.findById(id);
    }
    
    public Mesa salvar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }
    
    public Mesa atualizar(Long id, Mesa mesaAtualizada) {
        return mesaRepository.findById(id)
            .map(mesa -> {
                mesa.setNumero(mesaAtualizada.getNumero());
                mesa.setCapacidade(mesaAtualizada.getCapacidade());
                mesa.setStatus(mesaAtualizada.getStatus());
                mesa.setLocalizacao(mesaAtualizada.getLocalizacao());
                return mesaRepository.save(mesa);
            })
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));
    }
    
    public void deletar(Long id) {
        mesaRepository.deleteById(id);
    }
    
    public void atualizarStatus(Long id, StatusMesa novoStatus) {
        mesaRepository.findById(id)
            .ifPresent(mesa -> {
                mesa.setStatus(novoStatus);
                mesaRepository.save(mesa);
            });
    }
}
