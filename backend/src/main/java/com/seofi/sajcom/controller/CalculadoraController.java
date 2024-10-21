package com.seofi.sajcom.controller;

import com.seofi.sajcom.domain.DividaCalculadaDTO;
import com.seofi.sajcom.domain.DividaDTO;
import com.seofi.sajcom.service.CalculadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/")
public class CalculadoraController {
    @Autowired
    private CalculadoraService calculadoraServ;

    @GetMapping("calcular")
    public ResponseEntity<DividaCalculadaDTO> mostrarResultado(@RequestParam  BigDecimal valor, @RequestParam  LocalDate dataInicial, @RequestParam  LocalDate dataFinal) throws IOException {
        DividaDTO divida = new DividaDTO(valor, dataInicial, dataFinal);
        DividaCalculadaDTO dividaCalculadaDTO = this.calculadoraServ.calcularDivida(divida);
        return ResponseEntity.ok().body(dividaCalculadaDTO);
    }
}