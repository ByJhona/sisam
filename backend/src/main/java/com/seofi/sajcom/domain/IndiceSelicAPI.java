package com.seofi.sajcom.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Setter
@Getter
@Entity
@Table(name = "selic_mes")
@NoArgsConstructor
public class SelicMes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "data")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate data;
    @Column(name = "valor",precision = 10 , scale = 6)
    BigDecimal valor;


    public SelicMes(LocalDate data, BigDecimal valor) {
        this.data = data;
        this.valor = new BigDecimal(valor.toString());
    }

    @JsonCreator
    public SelicMes(@JsonProperty("data") String dataString, @JsonProperty("valor") BigDecimal valor) {
        this.data = converterDataString(dataString);
        this.valor = new BigDecimal(valor.toString());
    }

    private LocalDate converterDataString(String dataString) {
        DateTimeFormatter formatterAPI = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(dataString, formatterAPI);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dataString, formatter);
        } // Fazer tratamento de erro
    }

    @Override
    public String toString() {
        return "Valor: " + this.valor + " Data: " + this.data;
    }
}