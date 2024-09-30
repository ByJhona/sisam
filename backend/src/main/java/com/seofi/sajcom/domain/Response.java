package com.seofi.sajcom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class Response {
    private int codigo;
    private Map<String, String> mensagem;
}
