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
