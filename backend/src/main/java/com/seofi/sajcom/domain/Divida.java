package com.seofi.sajcom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Setter
@Getter
@AllArgsConstructor
public class Divida {
    float valor;
    LocalDate dataInicial;
    LocalDate dataFinal;
}
