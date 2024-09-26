package com.seofi.sajcom.domain;


import java.math.BigDecimal;
import java.util.List;

public record DividaDTO(
        BigDecimal valor,
        List<SelicAcumuladaDTO> indices
) { }
