package com.seofi.sajcom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Setter
@Getter
@Entity
@Table(name = "selic_acumulada")
public class SelicAcumulada {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "data")
    LocalDate data;
    @Column(name = "valor", precision = 10, scale = 6)
    BigDecimal valor;

    public SelicAcumulada(LocalDate data, BigDecimal valor) {
        this.data = data;
        this.valor = new BigDecimal(valor.toString());
    }

    @Override
    public String toString() {
        return "Valor: " + this.valor + " Data: " + this.data;
    }
}