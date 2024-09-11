package com.seofi.sajcom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.Divida;
import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.repository.IndiceRepository;
import com.seofi.sajcom.service.BacenAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CalculadoraController {
    @Autowired
    private BacenAPI api;
    @Autowired
    private IndiceRepository indiceRepository;

    @PostMapping("calcular")
    public ResponseEntity<Divida> mostrarResultado(@RequestBody Divida divida) throws JsonProcessingException {

        System.out.println(divida.getValor());
        System.out.println(divida.getDataInicial());
        this.api.filtrarIndicesSelic();



        return ResponseEntity.ok().body(divida);
    }
}