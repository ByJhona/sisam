package com.seofi.sajcom.repository;

import com.seofi.sajcom.domain.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface IndiceRepository extends JpaRepository<Indice, Long> {

    @Query("""
            select EXISTS(
                select indice from Indice indice
                where
                    indice.data = :data
            )
            """)
    boolean existeNoBanco(LocalDate data);
}
