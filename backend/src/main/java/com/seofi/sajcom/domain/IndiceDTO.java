package com.seofi.sajcom.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IndiceDTO(
        @NotNull(message = "A data dívida precisa ser informada.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate data,
        @NotNull(message = "O valor da dívida precisa ser informado.")
        BigDecimal valor,
        @NotNull(message = "O tipo da dívida precisa ser informado.")
        String tipo
) {
}
