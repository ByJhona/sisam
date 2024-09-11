package com.seofi.sajcom.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.repository.IndiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BacenAPI {
    private final WebClient client;

    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private IndiceRepository indiceRepository;

    public BacenAPI(WebClient.Builder clientBuilder){
        this.client = clientBuilder.baseUrl("https://api.bcb.gov.br").build();
    }

    public Mono<Indice[]> getIndices() {
        return this.client.get().uri("/dados/serie/bcdata.sgs.4390/dados?formato=json").retrieve().bodyToMono(Indice[].class);
        //return jsonMapper.readValue(indicesString, new TypeReference<List<Indice>>() {
        //});
        // A api pode retornar um html, tratar disso
        // Null pointer
    }

    public void filtrarIndicesSelic() {
        LocalDate dataSelicRef = LocalDate.of(2021, 11, 1);
        Indice[] indices = this.getIndices().block();

        for(Indice indice : indices){
            System.out.println(indice);
            LocalDate dataIndice = indice.getData();
            boolean existeNoBanco = this.indiceRepository.existeNoBanco(dataIndice);
            if(dataSelicRef.isBefore(dataIndice) && !existeNoBanco){
              this.indiceRepository.save(indice);
            }
        }
    }
}