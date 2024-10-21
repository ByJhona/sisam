package com.seofi.sajcom.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class Divida {
    @NotNull(message = "O valor da dívida precisa ser informado.")
    BigDecimal valor;
    @NotNull(message = "A data inicial precisa ser informada.")
    LocalDate dataInicial;
    @NotNull(message = "A data final precisa ser informada.")
    LocalDate dataFinal;
}
