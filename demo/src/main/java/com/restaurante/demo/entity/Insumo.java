package com.restaurante.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private Double quantidadeAtual;

    @Column(name = "unidade_medida")
    private String unidadeMedida;

    @Column(name = "preco_custo")
    private BigDecimal precoCusto;

    @Column(name = "estoque_seguranca")
    private Double estoqueSeguranca;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
}