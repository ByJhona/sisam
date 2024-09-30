package com.seofi.sajcom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.exception.IntervaloDatasInvalido;
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
public class Util {
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
    }

    public boolean verificarExisteNoBanco(List<SelicMes> indicesDoBanco, SelicMes indice) {
        return indicesDoBanco.stream().anyMatch(indiceDoBanco -> indiceDoBanco.getData().isEqual(indice.getData()));
    }



    public List<SelicAcumuladaDTO> buscarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal) {
        validarDataInicialFinal(dataInicial, dataFinal);
        List<SelicAcumuladaDTO> intervaloIndices = this.selicAcumuladaRepo.buscarIntervalo(dataInicial, dataFinal);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    private void validarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal, List<SelicAcumuladaDTO> intervaloIndices){
        intervaloIndices.forEach(indice -> {
            if((indice.data().isEqual(dataInicial) || indice.data().isEqual(dataFinal)) || (indice.data().isAfter(dataInicial) && indice.data().isBefore(dataFinal))){
                System.out.println("Datas válidas");
            }else{
                throw new IntervaloDatasInvalido();
            }
        });
    }

    private void validarDataInicialFinal(LocalDate dataInicial, LocalDate dataFinal){
        if(dataInicial.isAfter(dataFinal) || dataFinal.isBefore(dataInicial)){
            throw new IntervaloDatasInvalido();
        }
    }













}
