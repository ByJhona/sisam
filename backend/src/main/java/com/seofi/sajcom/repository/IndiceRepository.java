package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Indices;
import com.seofi.sajcom.domain.SelicAcumulada;
import com.seofi.sajcom.domain.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IndiceRepository extends JpaRepository<Indices, Long> {


    @Query(
            """
            select indice from Indices indice
            where
            indice.tipo = :tipo
            """
    )
    List<Indices> buscarIndices(Tipo tipo);

    @Query("""
            select indice from Indices indice
            where
            data >= :dataInicial and data <= :dataFinal and tipo = :tipo
            """
    )
    List<Indices> buscarIndicesIntervalo(LocalDate dataInicial, LocalDate dataFinal,Tipo tipo);


    @Query(
            """
            select indice from Indices indice
            where
            indice.tipo = :tipo and indice.data = :data
            """
    )
    Indices buscarIndice(LocalDate data, Tipo tipo);



    @Modifying
    @Query(value = """
            insert into indices (data, valor, id_tipo) values (:#{#indice.data}, :#{#indice.valor}, :#{#indice.tipo.id})
            on conflict(data, id_tipo)
            do update
            set valor = :#{#indice.valor}
            """, nativeQuery = true)
    void atualizarSelicAcumulada(Indices indice);

}
