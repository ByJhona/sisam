package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.FatorAtualizacao;
import com.seofi.sajcom.domain.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FatorIndiceRepository extends JpaRepository<FatorAtualizacao, Long> {

    @Query("""
            select new com.seofi.sajcom.domain.Indice(indice.data, indice.valor) from FatorIndice indice
            where
            data >= :dataInicial and data <= :dataFinal
            """)
    List<Indice> buscarIntervalo(LocalDate dataInicial, LocalDate dataFinal);

    @Query("""
            select new com.seofi.sajcom.domain.Indice(indice.data, indice.valor) from FatorIndice indice
            where
            data = :data
            """)
    Indice buscarIndice(LocalDate data);


}
