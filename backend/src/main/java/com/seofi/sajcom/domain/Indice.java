package com.seofi.sajcom.domain;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Indice(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate data,
        BigDecimal valor
) {
}
