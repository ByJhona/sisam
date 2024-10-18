package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.repository.IndiceRepository;
import com.seofi.sajcom.repository.TipoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@EnableScheduling
public class AtualizarSelicAcumulada {
    @Autowired
    private TipoRepository tipoRepo;
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private Util util;
    private static final String TIME_ZONE = "America/Sao_Paulo";
    private static final BigDecimal CEM = BigDecimal.valueOf(100);


    @Scheduled(zone = TIME_ZONE, cron = "30 * * * * ?")
    @Transactional
    public void atualizarIndicesSelicAcumulada() {
        Tipo tipoAcumulado = this.util.buscarTipo(EnumTipo.Acumulada.obterTipo());
        List<Indice> indicesSelicDTO = buscarIndices();
        BigDecimal valorSelicAcumulado = BigDecimal.ONE;

        for (Indice indice : indicesSelicDTO) {
            valorSelicAcumulado = calcular(indice, valorSelicAcumulado);
            salvar(indice, valorSelicAcumulado, tipoAcumulado);
        }
        System.out.println("Selic acumulada atualizada.");

    }

    private BigDecimal calcular(Indice indice, BigDecimal valorAcumulado) {
        BigDecimal valor = indice.getValor().divide(CEM,  RoundingMode.HALF_UP);
        return valorAcumulado.add(valor);
    }

    private void salvar(Indice indice, BigDecimal valor, Tipo tipo) {
        if (indice.getData() != null && valor != null) {
            Indice indiceNovo = new Indice(indice.getData(), valor, tipo);
            this.indiceRepo.atualizarSelicAcumulada(indiceNovo);
        }
    }

    private List<Indice> buscarIndices() {
        Tipo tipoMes = tipoRepo.buscarTipo(EnumTipo.Mes.obterTipo());
        List<Indice> indices = indiceRepo.buscarIndices(tipoMes);
        indices.sort((indice1, indice2) -> indice2.getData().compareTo(indice1.getData()));
        return indices;

    }
}
