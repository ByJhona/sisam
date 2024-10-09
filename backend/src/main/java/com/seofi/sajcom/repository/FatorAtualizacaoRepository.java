package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.FatorAtualizacao;
import com.seofi.sajcom.domain.FatoresAtualizacaoDTO;
import com.seofi.sajcom.domain.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FatorAtualizacaoRepository extends JpaRepository<FatorAtualizacao, Long> {

    @Query("""
            select new com.seofi.sajcom.domain.Indice(indice.data, indice.valor) from FatorAtualizacao indice
            where
            data >= :dataInicial and data <= :dataFinal
            """)
    List<Indice> buscarIntervalo(LocalDate dataInicial, LocalDate dataFinal);

    @Query("""
            select new com.seofi.sajcom.domain.Indice(indice.data, indice.valor) from FatorAtualizacao indice
            where
            data = :data
            """)
    Indice buscarIndice(LocalDate data);

}
