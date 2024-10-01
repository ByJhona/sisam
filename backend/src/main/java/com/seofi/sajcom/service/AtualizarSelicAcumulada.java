package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.SelicAcumulada;
import com.seofi.sajcom.domain.SelicMesDTO;
import com.seofi.sajcom.repository.SelicAcumuladaRepository;
import com.seofi.sajcom.repository.SelicMesRepository;
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
    private SelicAcumuladaRepository selicAcumuladaRepo;
    private static final String TIME_ZONE = "America/Sao_Paulo";

    @Scheduled(zone = TIME_ZONE, cron = "0 0 2 1 * ?")
    @Transactional
    public void atualizarIndicesSelicAcumulada() {
        List<SelicMesDTO> indicesSelicDTO = this.selicMesRepo.buscarIndices();
        BigDecimal valorSelicAcumulado = BigDecimal.ZERO;

        for (SelicMesDTO indice : indicesSelicDTO) {
            BigDecimal valor = indice.valor().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            LocalDate data = indice.data();
            valorSelicAcumulado = valorSelicAcumulado.add(valor).setScale(6, RoundingMode.HALF_UP);
            SelicAcumulada indiceAcumulado = new SelicAcumulada(data, valorSelicAcumulado);

            this.selicAcumuladaRepo.atualizarValor(indiceAcumulado);
        }
        System.out.println("Selic acumulada atualizada.");

    }
}
