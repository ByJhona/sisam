package com.seofi.sajcom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "fatores_indices")
public class FatorIndice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "data")
    LocalDate data;
    @Column(name = "valor", precision = 10, scale = 5)
    BigDecimal valor;

    public FatorIndice(LocalDate data, BigDecimal valor) {
        this.data = data;
        this.valor = new BigDecimal(valor.toString());
    }

    @Override
    public String toString() {
        return "Valor: " + this.valor + " Data: " + this.data;
    }
}