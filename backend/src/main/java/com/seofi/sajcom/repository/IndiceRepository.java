package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.domain.IndiceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IndiceRepository extends JpaRepository<Indice, Long> {

    @Query("""
            select EXISTS(
                select indice from Indice indice
                where
                    indice.data = :data
            )
            """)
    boolean existeNoBanco(LocalDate data);

    @Query("""
            select new com.seofi.sajcom.domain.IndiceDTO(indice.data, indice.valor) from Indice indice
            where
            data >= :dataInicial and data <= :dataFinal
            """)
    List<IndiceDTO> buscarIntervalo(LocalDate dataInicial, LocalDate dataFinal);
}
