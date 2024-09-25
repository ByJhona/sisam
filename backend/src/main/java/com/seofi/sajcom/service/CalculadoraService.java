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
    private SelicMesRepository selicMesRepo;
    @Autowired
    private SelicAcumuladaRepository selicAcumuladaRepo;
    @Autowired
    private BacenAPI bacenAPI;

    @Transactional
    public void filtrarIndicesSelic() throws JsonProcessingException {
        LocalDate dataInicialSelicRef = LocalDate.of(2021, 11, 1);
        LocalDate dataFinalSelicRef = LocalDate.now().withDayOfMonth(1);
        List<SelicMes> indicesAPI = bacenAPI.getIndices();
        List<SelicMes> indicesDoBanco = selicMesRepo.findAll();
        List<SelicMes> novosIndices = new ArrayList<>();

        for (SelicMes indice : indicesAPI) {
            LocalDate dataIndice = indice.getData();
            boolean existeNoBanco = verificarExisteNoBanco(indicesDoBanco, indice);
            if (!existeNoBanco && dataInicialSelicRef.isBefore(dataIndice) && dataFinalSelicRef.isAfter(dataIndice)) {
                BigDecimal valor = indice.getValor().setScale(6, RoundingMode.HALF_UP);
                SelicMes novoSelicMes = new SelicMes(indice.getData(), valor);
                novosIndices.add(novoSelicMes);
            }
        }
        if (!novosIndices.isEmpty()) {
            this.selicMesRepo.saveAll(novosIndices);
        }
        atualizarIndicesSelicAcumulada();
    }

    public boolean verificarExisteNoBanco(List<SelicMes> indicesDoBanco, SelicMes indice) {
        return indicesDoBanco.stream().anyMatch(indiceDoBanco -> indiceDoBanco.getData().isEqual(indice.getData()));
    }

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
    }

    public DividaDTO calcularDivida(Divida divida) {
        float valor = divida.getValor();
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();

        List<SelicMesDTO> intervaloDatas = this.buscarIntervaloDatas(dataInicial, dataFinal);

        return new DividaDTO(valor, intervaloDatas);
    }

    private List<SelicMesDTO> buscarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal) {
        //Verificar se as datas entao validas
        return this.selicMesRepo.buscarIntervalo(dataInicial, dataFinal);
    }
}
