package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.exception.IntervaloDatasInvalido;
import com.seofi.sajcom.repository.FatorAtualizacaoRepository;
import com.seofi.sajcom.repository.FatorIndiceRepository;
import com.seofi.sajcom.repository.SelicAcumuladaRepository;
import com.seofi.sajcom.repository.SelicMesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Util {
    @Autowired
    private SelicMesRepository selicMesRepo;
    @Autowired
    private FatorAtualizacaoRepository fatorAtualizacaoRepo;
    @Autowired
    private FatorIndiceRepository fatorIndiceRepo;
    @Autowired
    private SelicAcumuladaRepository selicAcumuladaRepo;
    @Autowired
    private BacenAPI bacenAPI;

    public List<Indice> intervaloSelicAcumulada(LocalDate dataInicial, LocalDate dataFinal) {
        validarDataInicialFinal(dataInicial, dataFinal);
        List<Indice> intervaloIndices = this.selicAcumuladaRepo.buscarIntervalo(dataInicial, dataFinal);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    public List<Indice> intervaloSelicMes(LocalDate dataInicial, LocalDate dataFinal) {
        validarDataInicialFinal(dataInicial, dataFinal);
        List<Indice> intervaloIndices = this.selicMesRepo.buscarIntervalo(dataInicial, dataFinal);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    public List<Indice> intervaloFatorAtualizacao(LocalDate dataInicial, LocalDate dataFinal) {
        validarDataInicialFinal(dataInicial, dataFinal);
        List<Indice> intervaloIndices = this.fatorAtualizacaoRepo.buscarIntervalo(dataInicial, dataFinal);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    public Indice indiceFatorAtualizacao(LocalDate data) {
        return this.fatorAtualizacaoRepo.buscarIndice(data);
    }
    public Indice indiceFatorIndice(LocalDate data) {
        return this.fatorIndiceRepo.buscarIndice(data);
    }

    public List<Indice> intervaloFatorIndice(LocalDate dataInicial, LocalDate dataFinal) {
        validarDataInicialFinal(dataInicial, dataFinal);
        List<Indice> intervaloIndices = this.fatorIndiceRepo.buscarIntervalo(dataInicial, dataFinal);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    private void validarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal, List<Indice> intervaloIndices){
        intervaloIndices.forEach(indice -> {
            if((indice.data().isEqual(dataInicial) || indice.data().isEqual(dataFinal)) || (indice.data().isAfter(dataInicial) && indice.data().isBefore(dataFinal))){

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
