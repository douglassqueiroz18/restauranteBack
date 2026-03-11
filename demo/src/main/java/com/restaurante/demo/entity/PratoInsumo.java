package com.restaurante.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prato_insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PratoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "prato_id")
    private Prato prato;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    @Column(nullable = false)
    private Double quantidadeNecessaria; // Ex: 0.150 (para 150g se o insumo for KG)

    @Column(name = "unidade_medida")
    private String unidadeMedida; // Adicionado o tipo String aqu
    }
