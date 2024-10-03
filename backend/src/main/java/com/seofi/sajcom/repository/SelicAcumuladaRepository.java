package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.SelicAcumulada;
import com.seofi.sajcom.domain.SelicAcumuladaDTO;
import com.seofi.sajcom.domain.SelicMes;
import com.seofi.sajcom.domain.SelicMesDTO;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SelicAcumuladaRepository extends JpaRepository<SelicAcumulada, Long> {

    @Query("""
            select EXISTS(
                select indice from SelicMes indice
                where
                    indice.data = :data
            )
            """)
    boolean existeNoBanco(LocalDate data);

    @Modifying
    @Query(value = """
            insert into selic_acumulada (data, valor) values (:#{#indice.data}, :#{#indice.valor})
            on conflict(data)
            do update
            set valor = :#{#indice.valor}
            """, nativeQuery = true)
    void atualizarValor(SelicAcumulada indice);

    @Query("""
            select new com.seofi.sajcom.domain.SelicAcumuladaDTO(indice.data, indice.valor) from SelicAcumulada indice
            where
            data >= :dataInicial and data <= :dataFinal
            """)
    List<SelicAcumuladaDTO> buscarIntervalo(LocalDate dataInicial, LocalDate dataFinal);

    @Query("""
            select new com.seofi.sajcom.domain.SelicAcumuladaDTO(indice.data, indice.valor) from SelicAcumulada indice
            """)
    List<SelicAcumuladaDTO> buscarTudo();

}
