package com.seofi.sajcom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.Divida;
import com.seofi.sajcom.domain.DividaDTO;
import com.seofi.sajcom.service.CalculadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CalculadoraController {
    @Autowired
    private CalculadoraService calculadoraServ;

    @PostMapping("calcular")
    public ResponseEntity<DividaDTO> mostrarResultado(@RequestBody Divida divida) throws JsonProcessingException {
        this.calculadoraServ.filtrarIndicesSelic();
        DividaDTO dividaDTO = this.calculadoraServ.calcularDivida(divida);
        return ResponseEntity.ok().body(dividaDTO);
    }
}