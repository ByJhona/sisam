package com.seofi.sajcom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seofi.sajcom.domain.Divida;
import com.seofi.sajcom.domain.DividaDTO;
import com.seofi.sajcom.service.CalculadoraService;
import com.seofi.sajcom.service.Relatorio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Autowired
    private Relatorio relatorio;


    @GetMapping("calcular")
    public ResponseEntity<DividaDTO> mostrarResultado(@RequestParam  BigDecimal valor, @RequestParam  LocalDate dataInicial, @RequestParam  LocalDate dataFinal) throws IOException {
        Divida divida = new Divida(valor, dataInicial, dataFinal);
        DividaDTO dividaDTO = this.calculadoraServ.calcularDivida(divida);
        return ResponseEntity.ok().body(dividaDTO);
    }

    @GetMapping("baixar-relatorio")
    public ResponseEntity<byte[]> baixarRelatorio(@RequestParam  LocalDate dataInicial, @RequestParam  LocalDate dataFinal) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        byte[] documentoBinario = relatorio.gerarPDF(dataInicial, dataFinal);

        return new ResponseEntity<>(documentoBinario, headers, HttpStatus.OK);
    }
}