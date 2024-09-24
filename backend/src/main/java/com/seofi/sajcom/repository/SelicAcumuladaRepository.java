package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.SelicAcumulada;
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
            insert into selic_acumulada (data, valor) values (:data, :valor)
            on conflict(data)
            do update
            set valor = :valor
            """, nativeQuery = true)
    void atualizarValor(LocalDate data, BigDecimal valor);
}
