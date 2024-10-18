package com.seofi.sajcom.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "indice")
public class Indices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "A data precisa ser informada.")
    @Column(name = "data")
    private LocalDate data;
    @NotNull(message = "O valor da dívida precisa ser informado.")
    @Column(name = "valor", precision = 32, scale = 15)
    private BigDecimal valor;
    @NotNull(message = "O tipo precisa ser informado.")
    @JoinColumn(name = "id_tipo", nullable = false)  // Chave estrangeira
    @ManyToOne // Relacionamento com Tipo
    private Tipo tipo;

    public Indices(LocalDate data, BigDecimal valor, Tipo tipo){
        this.id = null;
        this.data = data;
        this.valor = valor;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Valor: " + this.valor + " Data: " + this.data + " Tipo: " + this.tipo;
    }

}
