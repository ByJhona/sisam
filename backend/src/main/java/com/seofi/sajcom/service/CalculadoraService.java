package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.exception.IntervaloDatasInvalido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CalculadoraService {
    @Autowired
    private Util util;
    private static final LocalDate dataFinalFatores = LocalDate.of(2021, 11, 1);
    private static final LocalDate dataInicialSelic = LocalDate.of(2021, 12, 1);
    private static final BigDecimal CEM = BigDecimal.valueOf(100);


    public DividaCalculadaDTO calcularDivida(Divida divida) {
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();
        BigDecimal valor = divida.getValor();
        BigDecimal valorTotalCalculado = calcularMontanteTotal(dataInicial, dataFinal, valor);
        List<IndiceDTO> indices = buscarIndices(dataInicial, dataFinal);
        return new DividaCalculadaDTO(valor, valorTotalCalculado, dataInicial, dataFinal, indices);
    }

    private List<IndiceDTO> buscarIndices(LocalDate dataInicial, LocalDate dataFinal) {
        List<Indice> indicesSelic = this.util.buscarIntervaloIndices(dataInicial, dataFinal, EnumTipo.Acumulada);
        List<Indice> indices = new ArrayList<Indice>();

        adicionarFatores(dataInicial, indices, EnumTipo.Atualizacao, EnumTipo.Indice);

        if(!indicesSelic.isEmpty()){
            indices.addAll(indicesSelic);
        }
        ordenarIndicesPorData(indices);
        return this.util.converterIndicesParaDTO(indices);
    }
    private void ordenarIndicesPorData(List<Indice> indices) {
        indices.sort(Comparator.comparing(Indice::getData).reversed());
    }
    private void adicionarFatores(LocalDate dataInicial, List<Indice> indices, EnumTipo... tipos) {
        for (EnumTipo tipo : tipos) {
            Indice indice = util.buscarIndice(dataInicial, tipo);
            if (indice != null) {
                indices.add(indice);
            }
        }
    }

    private BigDecimal calcularMontanteTotal(LocalDate dataInicial, LocalDate dataFinal, BigDecimal valor) {
        BigDecimal valorTotal = BigDecimal.ZERO;
        if (dataInicial.isAfter(dataFinalFatores)) {
            valorTotal = calculoValorSelicAcumulada(dataInicial, dataFinal, valor);
        } else if (dataInicial.isBefore(dataFinalFatores) || dataInicial.isEqual(dataFinalFatores)) {
            BigDecimal valorFatores = calculoValorFatores(dataInicial, valor);
            valorTotal = calculoValorSelicAcumulada(dataInicialSelic, dataFinal, valorFatores);
        }

        return valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculoValorFatores(LocalDate data, BigDecimal valor) {
        Indice indiceFatorAtualizacao = util.buscarIndice(data, EnumTipo.Atualizacao);
        Indice indiceFatorIndice = util.buscarIndice(data, EnumTipo.Indice);

        return calcularValorFatoresNOVO(valor, indiceFatorAtualizacao, indiceFatorIndice);
    }

    private BigDecimal calcularValorFatoresNOVO(BigDecimal valor, Indice indiceFatorAtualizacao, Indice indiceFatorIndice) {

        if (indiceFatorAtualizacao == null || indiceFatorIndice == null) {
            return valor;
        }

        BigDecimal coefFatorAtualizacao = indiceFatorAtualizacao.getValor();
        BigDecimal taxaFatorIndice = (indiceFatorIndice.getValor().divide(CEM, RoundingMode.HALF_UP));
        BigDecimal valorFatorAtualizacao = valor.multiply(coefFatorAtualizacao);
        BigDecimal valorJurosFatorIndice = valorFatorAtualizacao.multiply(taxaFatorIndice);

        return valorFatorAtualizacao.add(valorJurosFatorIndice);
    }

    private BigDecimal calculoValorSelicAcumulada(LocalDate dataInicial, LocalDate dataFinal, BigDecimal valor) {
        Indice indiceInicial = util.buscarIndice(dataInicial, EnumTipo.Acumulada);
        Indice indiceFinal = util.buscarIndice(dataFinal, EnumTipo.Acumulada);

        if(indiceInicial == null || indiceFinal == null){
            throw new IntervaloDatasInvalido("Índices não poem ser nulos.");
        }

        return calcularValoresSelicAcumulada(valor, indiceInicial, indiceFinal);
    }

    private BigDecimal calcularValoresSelicAcumulada(BigDecimal valor, Indice indiceInicial, Indice indiceFinal) {
        BigDecimal taxaInicial = indiceInicial.getValor();
        BigDecimal taxaFinal = indiceFinal.getValor();
        BigDecimal taxa = taxaInicial.subtract(taxaFinal);
        BigDecimal valorTaxaCalculada = valor.multiply(taxa);

        return valor.add(valorTaxaCalculada);
    }

}
