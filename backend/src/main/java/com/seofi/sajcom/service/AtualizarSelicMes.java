package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.Indices;
import com.seofi.sajcom.domain.SelicMes;
import com.seofi.sajcom.domain.Tipo;
import com.seofi.sajcom.repository.IndiceRepository;
import com.seofi.sajcom.repository.TipoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class AtualizarSelicMes {
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private TipoRepository tipoRepo;
    @Autowired
    private BacenAPI bacenAPI;
    @Autowired
    private Util util;
    private static final String TIME_ZONE = "America/Sao_Paulo";
    private static final LocalDate dataInicialSelicRef = LocalDate.of(2021, 11, 1);
    private static final LocalDate dataFinalSelicRef = LocalDate.now().withDayOfMonth(1);

    @Scheduled(zone = TIME_ZONE, cron = "50 * * * * ?")
    @Transactional
    public void filtrarIndicesSelic() {

        List<SelicMes> indicesAPI = bacenAPI.getIndices();
        List<Indices> novosIndices = new ArrayList<>();
        Tipo tipoMes = this.util.buscarTipo(EnumTipo.Mes.obterTipo());
        List<Indices>  indicesDoBanco = indiceRepo.buscarIndices(tipoMes);

        for (SelicMes indice : indicesAPI) {
            LocalDate dataIndice = indice.getData();
            boolean existeNoBanco = verificarExisteNoBanco(indicesDoBanco, indice);
            if (!existeNoBanco && dataInicialSelicRef.isBefore(dataIndice) && dataFinalSelicRef.isAfter(dataIndice)) {
                BigDecimal valor = indice.getValor().setScale(6, RoundingMode.HALF_UP);
                Indices novoSelicMes = new Indices(indice.getData(), valor, tipoMes);
                novosIndices.add(novoSelicMes);
            }
        }
        if (!novosIndices.isEmpty()) {
            this.indiceRepo.saveAll(novosIndices);
        }

        System.out.println("Selic mes atualizada.");
    }
    private boolean verificarExisteNoBanco(List<Indices> indicesDoBanco, SelicMes indice) {
        return indicesDoBanco.stream().anyMatch(indiceDoBanco -> indiceDoBanco.getData().isEqual(indice.getData()));
    }
}
