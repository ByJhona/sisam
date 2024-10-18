package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Tipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TipoRepository extends JpaRepository<Tipo, Long> {


    @Query(
            """
            select tipo from Tipo tipo
            where
            descricao = :descricao
            """
    )
    Tipo buscarTipo(String descricao);
}
