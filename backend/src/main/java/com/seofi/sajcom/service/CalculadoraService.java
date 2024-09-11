package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.Divida;
import com.seofi.sajcom.domain.DividaDTO;
import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.domain.IndiceDTO;
import com.seofi.sajcom.repository.IndiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalculadoraService {
    @Autowired
    private IndiceRepository indiceRepo;
    @Autowired
    private BacenAPI bacenAPI;

    @Transactional
    public void filtrarIndicesSelic() {
        LocalDate dataSelicRef = LocalDate.of(2021, 11, 1);
        List<Indice> indices = bacenAPI.getIndices();

        for(Indice indice : indices){
            LocalDate dataIndice = indice.getData();
            boolean existeNoBanco = this.indiceRepo.existeNoBanco(dataIndice);
            if(dataSelicRef.isBefore(dataIndice) && !existeNoBanco){
                this.indiceRepo.save(indice);
            }
        }
    }

    public DividaDTO calcularDivida(Divida divida){
        float valor = divida.getValor();
        LocalDate dataInicial = divida.getDataInicial();
        LocalDate dataFinal = divida.getDataFinal();

        List<IndiceDTO> intervaloDatas = this.buscarIntervaloDatas(dataInicial, dataFinal);

        return new DividaDTO(valor, intervaloDatas);


    }

    private List<IndiceDTO> buscarIntervaloDatas(LocalDate dataInicial, LocalDate dataFinal){
        //Verificar se as datas entao validas
        return this.indiceRepo.buscarIntervalo(dataInicial, dataFinal);
    }
}
