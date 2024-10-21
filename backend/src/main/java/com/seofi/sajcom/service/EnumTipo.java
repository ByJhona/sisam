package com.seofi.sajcom.service;

public enum EnumTipo {
    Acumulada("Selic Acumulada"), Mes("Selic Mes"), Atualizacao("Fator Atualizacao"), Indice("Fator Indice");

    private String descTipo;

    EnumTipo(String desc) {
        descTipo = desc;
    }

    public String obterTipo(){
        return descTipo;
    }
}
