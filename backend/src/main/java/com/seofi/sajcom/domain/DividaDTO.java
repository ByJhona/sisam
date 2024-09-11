package com.seofi.sajcom.domain;


import java.util.List;

public record DividaDTO(
        float valor,
        List<IndiceDTO> indices
) { }
