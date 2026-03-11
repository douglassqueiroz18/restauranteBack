package com.restaurante.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pedido")
public class ItensPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference // Evita loop no JSON (Angular)
    private Pedido pedido;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prato_id", nullable = false)
    private Prato prato;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @Column(nullable = false)
    private BigDecimal precoUnitario;
    
    @Column(nullable = false)
    private BigDecimal subtotal;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @PrePersist
    protected void onCreate() {
        if(subtotal == null && quantidade != null && precoUnitario != null) {
            subtotal = precoUnitario.multiply(new BigDecimal(quantidade));
        }
    }
    private String status;
}
