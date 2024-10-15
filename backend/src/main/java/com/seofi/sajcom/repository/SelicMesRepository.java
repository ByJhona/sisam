package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.domain.SelicMes;
import com.seofi.sajcom.domain.SelicMesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SelicMesRepository extends JpaRepository<SelicMes, Long> {

    @Query("""
            select EXISTS(
                select indice from SelicMes indice
                where
                    indice.data = :data
            )
            """)
    boolean existeNoBanco(LocalDate data);

    @Query("""
            select new com.seofi.sajcom.domain.Indice(indice.data, indice.valor) from SelicMes indice
            where
            data >= :dataInicial and data <= :dataFinal
            """)
    List<Indice> buscarIntervalo(LocalDate dataInicial, LocalDate dataFinal);

    @Query("""
            select new com.seofi.sajcom.domain.SelicMesDTO(indice.data, indice.valor) from SelicMes indice 
            ORDER BY data DESC
            """)
    List<SelicMesDTO> buscarIndices();
}
