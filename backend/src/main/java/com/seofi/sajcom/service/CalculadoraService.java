package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculadoraService {
    @Autowired
    private Util util;

    public DividaCalculadaDTO calcularDivida(Divida divida) {
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();
        dataFinal = dataFinal.minusMonths(1);
        BigDecimal valor = divida.getValor();
        BigDecimal valorTotalCalculado =  calcularMontanteTotal(dataInicial, dataFinal, valor);
        List<Indice> listaIndices = obterListaIndices(dataInicial, dataFinal);
        System.out.println(listaIndices);

       return new DividaCalculadaDTO(valor, valorTotalCalculado, dataInicial, dataFinal, listaIndices);
    }

    private BigDecimal calcularMontanteTotal(LocalDate dataInicial, LocalDate dataFinal, BigDecimal valor) {
        Indice indiceFatorAtualizacao = this.util.indiceFatorAtualizacao(dataInicial);
        Indice indiceFatorIndice = this.util.indiceFatorIndice(dataInicial);
        List<Indice> indicesSelicMes = this.util.intervaloSelicMes(dataInicial, dataFinal);

        BigDecimal valorCalculadoFatores = calcularValoresFatores(valor, indiceFatorAtualizacao, indiceFatorIndice);
        return  calcularValoresSelic( valorCalculadoFatores, indicesSelicMes);
    }

    private List<Indice> obterListaIndices(LocalDate dataInicial, LocalDate dataFinal){
        Indice indiceFatorAtualizacao = this.util.indiceFatorAtualizacao(dataInicial);
        Indice indiceFatorIndice = this.util.indiceFatorIndice(dataInicial);
        List<Indice> indicesSelicMes = this.util.intervaloSelicAcumulada(dataInicial, dataFinal);
        List<Indice> lista  = new ArrayList<>();
        lista.addAll(indicesSelicMes);


        return lista;
    }

    private BigDecimal calcularValoresSelic(BigDecimal valor, List<Indice> indicesSelicMes) {

        BigDecimal taxaAcumulada = BigDecimal.ZERO;

        for (Indice indice : indicesSelicMes) {
            BigDecimal valorMes = indice.valor();
            BigDecimal taxaMes = valorMes.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            taxaAcumulada = taxaAcumulada.add(taxaMes);
        }
        BigDecimal valorTaxaCalculada = valor.multiply(taxaAcumulada);
        return valor.add(valorTaxaCalculada);
    }

    private BigDecimal calcularValoresFatores(BigDecimal valor, Indice indiceFatorAtualizacao, Indice indiceFatorIndice) {

        if (indiceFatorAtualizacao == null || indiceFatorIndice == null) {
            return valor;
        }

        BigDecimal coefFatorAtualizacao = indiceFatorAtualizacao.valor();
        BigDecimal taxaFatorIndice = (indiceFatorIndice.valor().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
        BigDecimal valorFatorAtualizacao = valor.multiply(coefFatorAtualizacao);
        BigDecimal valorJurosFatorIndice = valorFatorAtualizacao.multiply(taxaFatorIndice);
        BigDecimal valorTotal = valorFatorAtualizacao.add(valorJurosFatorIndice);

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }


}
