package com.seofi.sajcom.service;

import com.seofi.sajcom.domain.IndiceSelicAPI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class BacenAPI {
    private final WebClient client;

    public BacenAPI(WebClient.Builder clientBuilder) {
        this.client = clientBuilder.baseUrl("https://api.bcb.gov.br").build();
    }

    @Retryable(retryFor = UnsupportedMediaTypeException.class, maxAttempts = 5,  backoff = @Backoff(delay = 1000))
    public List<IndiceSelicAPI> getIndices() {
        return this.client.get().uri("/dados/serie/bcdata.sgs.4390/dados?formato=json").header("Accept", "application/json").retrieve().bodyToMono(new ParameterizedTypeReference<List<IndiceSelicAPI>>() {
        }).block();
    }
}