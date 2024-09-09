package com.seofi.sajcom.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seofi.sajcom.domain.Indice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class BacenAPI {
    private final WebClient client;

    @Autowired
    private ObjectMapper jsonMapper;

    public BacenAPI(WebClient.Builder clientBuilder){
        this.client = clientBuilder.baseUrl("https://api.bcb.gov.br").build();
    }

    public List<Indice> getIndice() throws JsonProcessingException {
        String indicesString = this.client.get().uri("/dados/serie/bcdata.sgs.4390/dados?formato=json").retrieve().bodyToMono(String.class).block();

        return jsonMapper.readValue(indicesString, new TypeReference<List<Indice>>() {
        });

    }
}
