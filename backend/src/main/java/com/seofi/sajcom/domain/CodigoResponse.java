package com.seofi.sajcom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class CodigoResponse {
    private int codigo;
    private Map<String, String> mensagem;
}
