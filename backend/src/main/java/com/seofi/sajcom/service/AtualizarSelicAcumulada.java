package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.repository.IndiceRepository;
import com.seofi.sajcom.repository.SelicAcumuladaRepository;
import com.seofi.sajcom.repository.SelicMesRepository;
import com.seofi.sajcom.repository.TipoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
public class AtualizarSelicAcumulada {
    @Autowired
    private SelicMesRepository selicMesRepo;
    @Autowired
    private TipoRepository tipoRepo;
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private SelicAcumuladaRepository selicAcumuladaRepo;
    private static final String TIME_ZONE = "America/Sao_Paulo";

    @Scheduled(zone = TIME_ZONE, cron = "30 * * * * ?")
    @Transactional
    public void atualizarIndicesSelicAcumulada() {
        Tipo tipoMes = tipoRepo.buscarTipo(EnumTipo.Mes.obterTipo());
        Tipo tipoAcumulado = tipoRepo.buscarTipo(EnumTipo.Acumulada.obterTipo());
        List<Indices> indicesSelicDTO = indiceRepo.buscarTodosSelicMes(tipoMes);
        BigDecimal valorSelicAcumulado = BigDecimal.ZERO;

        for (Indices indice : indicesSelicDTO) {
            BigDecimal valor = indice.getValor().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            LocalDate data = indice.getData();
            valorSelicAcumulado = valorSelicAcumulado.add(valor).setScale(6, RoundingMode.HALF_UP);
            Indices indiceAcumulado = new Indices(data, valorSelicAcumulado, tipoAcumulado);

            this.indiceRepo.atualizarSelicAcumulada(indiceAcumulado);
        }
        System.out.println("Selic acumulada atualizada.");

    }
}
