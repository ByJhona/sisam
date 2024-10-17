package com.seofi.sajcom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.Indices;
import com.seofi.sajcom.domain.SelicMes;
import com.seofi.sajcom.domain.Tipo;
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
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class AtualizarSelicMes {
    @Autowired
    private SelicMesRepository selicMesRepo;
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private TipoRepository tipoRepo;
    @Autowired
    private SelicAcumuladaRepository selicAcumuladaRepo;
    @Autowired
    private BacenAPI bacenAPI;
    private static final String TIME_ZONE = "America/Sao_Paulo";


    @Scheduled(zone = TIME_ZONE, cron = "59 * * * * ?")
    @Transactional
    public void filtrarIndicesSelic() throws JsonProcessingException {
        LocalDate dataInicialSelicRef = LocalDate.of(2021, 11, 1);
        LocalDate dataFinalSelicRef = LocalDate.now().withDayOfMonth(1);
        List<SelicMes> indicesAPI = bacenAPI.getIndices();
        //List<SelicMes> indicesDoBanco = selicMesRepo.findAll();
        List<Indices> novosIndices = new ArrayList<>();
        Tipo tipo = tipoRepo.buscarTipo(EnumTipo.Mes.obterTipo());
        List<Indices>  indicesDoBanco = indiceRepo.buscarTodosSelicMes(tipo);


        for (SelicMes indice : indicesAPI) {
            LocalDate dataIndice = indice.getData();
            boolean existeNoBanco = verificarExisteNoBanco(indicesDoBanco, indice);
            if (!existeNoBanco && dataInicialSelicRef.isBefore(dataIndice) && dataFinalSelicRef.isAfter(dataIndice)) {
                BigDecimal valor = indice.getValor().setScale(6, RoundingMode.HALF_UP);
                Indices novoSelicMes = new Indices(indice.getData(), valor, tipo);
                novosIndices.add(novoSelicMes);
            }
        }
        if (!novosIndices.isEmpty()) {
            this.indiceRepo.saveAll(novosIndices);
        }

        System.out.println("Selic mes atualizada.");
    }
 // private
    public boolean verificarExisteNoBanco(List<Indices> indicesDoBanco, SelicMes indice) {
        return indicesDoBanco.stream().anyMatch(indiceDoBanco -> indiceDoBanco.getData().isEqual(indice.getData()));
    }
}
