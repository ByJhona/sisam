package com.sajcom.sajcom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CalculadoraController {

    @PostMapping("calcular")
    public String mostrarResultado(){

        System.out.println();
        return "Olá, Mundo!";
    }

}
