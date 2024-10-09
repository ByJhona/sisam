package com.seofi.sajcom.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FatoresAtualizacaoDTO(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate data,
        BigDecimal valor
) {
}
