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
        List<SelicAcumuladaDTO> intervaloDatas = this.util.buscarIntervaloDatas(dataInicial, dataFinal);
        BigDecimal total = calcularMontanteTotal(valor, intervaloDatas);

        return new DividaDTO(valor, total, dataInicial, dataFinal, intervaloDatas);
    }

    private BigDecimal calcularMontanteTotal( BigDecimal total, List<SelicAcumuladaDTO> intervaloDatas){
        BigDecimal jurosAcumulado = BigDecimal.ZERO;

        for (SelicAcumuladaDTO indice : intervaloDatas){
            BigDecimal aux = total.multiply(indice.valor());
            jurosAcumulado = jurosAcumulado.add(aux);
            System.out.println(jurosAcumulado);
        }
        return total.add(jurosAcumulado);
    }

}
