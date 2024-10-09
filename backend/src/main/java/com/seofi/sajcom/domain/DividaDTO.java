package com.seofi.sajcom.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DividaDTO(
        BigDecimal valorInicial,
        BigDecimal valorFinal,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataInicial,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataFinal,
        List<Indice> indices
) { }
