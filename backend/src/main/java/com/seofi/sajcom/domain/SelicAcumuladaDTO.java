package com.seofi.sajcom.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


public record SelicAcumuladaDTO(@Column(name = "data")
                                @JsonFormat(pattern = "dd/MM/yyyy")
                                LocalDate data,
                                @Column(name = "valor")
                                BigDecimal valor) {}