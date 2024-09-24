package com.seofi.sajcom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate data;
    @Column(name = "valor",precision = 10 , scale = 6)
    BigDecimal valor;

    public SelicAcumulada(String data, BigDecimal valor){
        DateTimeFormatter formatterAPI = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            this.data = LocalDate.parse(data, formatterAPI);
        }catch ( DateTimeParseException e){
            this.data = LocalDate.parse(data, formatter);
        }
        this.valor = new BigDecimal(valor.toString());
    }

    @Override
    public String toString() {
        return "Valor: " + this.valor + " Data: " + this.data;
    }
}