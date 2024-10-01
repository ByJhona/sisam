package com.seofi.sajcom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.Divida;
import com.seofi.sajcom.domain.DividaDTO;
import com.seofi.sajcom.service.CalculadoraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/")
public class CalculadoraController {
    @Autowired
    private CalculadoraService calculadoraServ;

    @GetMapping("calcular")
    public ResponseEntity<DividaDTO> mostrarResultado(@RequestParam  BigDecimal valor, @RequestParam  LocalDate dataInicial, @RequestParam  LocalDate dataFinal){
        Divida divida = new Divida(valor, dataInicial, dataFinal);
        DividaDTO dividaDTO = this.calculadoraServ.calcularDivida(divida);
        return ResponseEntity.ok().body(dividaDTO);
    }
}