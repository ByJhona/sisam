package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Indice;
import com.seofi.sajcom.domain.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IndiceRepository extends JpaRepository<Indice, Long> {


    @Query(
            """
            select indice from Indice indice
            where
            indice.tipo = :tipo
            """
    )
    List<Indice> buscarIndices(Tipo tipo);

    @Query("""
            select indice from Indice indice
            where
            data >= :dataInicial and data <= :dataFinal and tipo = :tipo
            """
    )
    List<Indice> buscarIndicesIntervalo(LocalDate dataInicial, LocalDate dataFinal, Tipo tipo);



    @Query(
            """
            select indice from Indice indice
            where
            indice.tipo = :tipo and indice.data = :data
            """
    )
    Indice buscarIndice(LocalDate data, Tipo tipo);



    @Modifying
    @Query(value = """
            insert into indice (data, valor, id_tipo) values (:#{#indice.data}, :#{#indice.valor}, :#{#indice.tipo.id})
            on conflict(data, id_tipo)
            do update
            set valor = :#{#indice.valor}
            """, nativeQuery = true)
    void atualizarSelicAcumulada(Indice indice);

}
