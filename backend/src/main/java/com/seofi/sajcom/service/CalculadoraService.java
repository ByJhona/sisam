package com.seofi.sajcom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.repository.SelicAcumuladaRepository;
import com.seofi.sajcom.repository.SelicMesRepository;
import jakarta.transaction.Transactional;
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

    public DividaDTO calcularDivida(Divida divida) {
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();
        BigDecimal valor = divida.getValor();

        BigDecimal total = calcularMontanteTotal(dataInicial, dataFinal, valor);

        return new DividaDTO(valor, total, dataInicial, dataFinal, new ArrayList<Indice>());
    }

    public List<Indice> testar(Divida divida) {
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();
        BigDecimal valor = divida.getValor();
        return this.util.intervaloFatorIndice(dataInicial, dataFinal);
    }

    //Refatorar pq achei estranho, falta algo, um plus, um negocinho
    private BigDecimal calcularMontanteTotal(LocalDate dataInicial, LocalDate dataFinal, BigDecimal valor) {
        BigDecimal valorCalculadoFatores = calcularValoresFatores(dataInicial, valor);
        return calcularValoresSelic(dataInicial, dataFinal, valorCalculadoFatores);
    }

    private BigDecimal calcularValoresSelic(LocalDate dataInicial, LocalDate dataFinal, BigDecimal valor) {
        List<Indice> indicesSelicAcumulada = this.util.intervaloSelicMes(dataInicial, dataFinal);
        BigDecimal taxaAcumulada = BigDecimal.ZERO;

        for (Indice indice : indicesSelicAcumulada) {
            BigDecimal valorMes = indice.valor();
            BigDecimal taxaMes = valorMes.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            taxaAcumulada = taxaAcumulada.add(taxaMes);
        }
        BigDecimal valorTaxaCalculada = valor.multiply(taxaAcumulada);
        return valor.add(valorTaxaCalculada);
    }

    private BigDecimal calcularValoresFatores(LocalDate data, BigDecimal valor) {
        Indice indiceFatorAtualizacao = this.util.indiceFatorAtualizacao(data);
        Indice indiceFatorIndice = this.util.indiceFatorIndice(data);

        if (indiceFatorAtualizacao != null && indiceFatorIndice != null) {
            BigDecimal coefFatorAtualizacao = indiceFatorAtualizacao.valor();
            BigDecimal taxaFatorIndice = (indiceFatorIndice.valor().divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            BigDecimal valorFatorAtualizacao = valor.multiply(coefFatorAtualizacao);
            BigDecimal valorJurosFatorIndice = valorFatorAtualizacao.multiply(taxaFatorIndice);
            BigDecimal valorTotal = valorFatorAtualizacao.add(valorJurosFatorIndice);
            System.out.println(valorTotal);

            return valorTotal.setScale(2, RoundingMode.HALF_UP);
        }

        return valor;
    }


}
