package com.seofi.sajcom.exception;

import com.seofi.sajcom.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TratadorExceptions {

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    public ResponseEntity<Response> tratarUnsupportedMediaTypeException() {
        Map<String, String> erro = new HashMap<>();
        erro.put("erro", "Api do BACEN indisponivel.");
        Response erroResponse =  new Response(1, erro);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(erroResponse);
    }
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> tratarMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            erros.put(error.getField(), error.getDefaultMessage());
        });

        Response erro =  new Response(2, erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(IntervaloDatasInvalido.class)
    public ResponseEntity<Response> tratarIntervaloDatasInvalido(IntervaloDatasInvalido ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());

        Response erroResp =  new Response(2, erros);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResp);
    }



}
