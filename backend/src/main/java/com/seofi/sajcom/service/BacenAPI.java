package com.seofi.sajcom.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seofi.sajcom.domain.SelicMes;
import com.seofi.sajcom.repository.SelicMesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class BacenAPI {
    private final WebClient client;
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private SelicMesRepository selicMesRepository;

    public BacenAPI(WebClient.Builder clientBuilder){
        this.client = clientBuilder.baseUrl("https://api.bcb.gov.br").build();
    }

    public List<SelicMes> getIndices() {
        return this.client.get().uri("/dados/serie/bcdata.sgs.4390/dados?formato=json").retrieve().bodyToMono(new ParameterizedTypeReference<List<SelicMes>>(){}).block();
        //return jsonMapper.readValue(indicesString, new TypeReference<List<Indice>>() {
        //});
        // A api pode retornar um html, tratar disso
        // Null pointer
    }
}