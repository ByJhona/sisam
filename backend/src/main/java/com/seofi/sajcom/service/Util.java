package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.*;
import com.seofi.sajcom.exception.IntervaloDatasInvalido;
import com.seofi.sajcom.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class Util {
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private TipoRepository tipoRepo;


    public List<Indices> buscarIntervaloIndices(LocalDate dataInicial, LocalDate dataFinal, EnumTipo tipo ) {
        Tipo tipoBanco = tipoRepo.buscarTipo(tipo.obterTipo());
        validarDataInicialFinal(dataInicial, dataFinal);
        List<Indices> intervaloIndices = this.indiceRepo.buscarIndicesIntervalo(dataInicial, dataFinal, tipoBanco);
        validarIntervaloDatas(dataInicial, dataFinal, intervaloIndices);
        return intervaloIndices;
    }

    public Indices buscarIndice(LocalDate data, EnumTipo tipoEnum){
        Tipo tipo = tipoRepo.buscarTipo(tipoEnum.obterTipo());
        return this.indiceRepo.buscarIndice(data, tipo);
    }

    public Tipo buscarTipo(String descricao){
        return this.tipoRepo.buscarTipo(descricao);
    }

    private void validarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal, List<Indices> intervaloIndices) {
        intervaloIndices.forEach(indice -> {
            if (!((indice.getData().isEqual(dataInicial) || indice.getData().isEqual(dataFinal)) || (indice.getData().isAfter(dataInicial) && indice.getData().isBefore(dataFinal)))) {
                throw new IntervaloDatasInvalido();
            }
        });
    }

    private void validarDataInicialFinal(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial.isAfter(dataFinal) || dataFinal.isBefore(dataInicial)) {
            throw new IntervaloDatasInvalido();
        }
    }

}
