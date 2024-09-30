package com.seofi.sajcom.exception;

public class IntervaloDatasInvalido extends RuntimeException {

    public IntervaloDatasInvalido(){
        super("O intervalo de datas é invalido!");
    }
    public IntervaloDatasInvalido(String mensagem){
        super(mensagem);
    }
}
