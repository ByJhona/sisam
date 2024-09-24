package com.seofi.sajcom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SelicMesDTO(
        @Column(name = "data")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate data,
        @Column(name = "valor")
        BigDecimal valor

) {
}
