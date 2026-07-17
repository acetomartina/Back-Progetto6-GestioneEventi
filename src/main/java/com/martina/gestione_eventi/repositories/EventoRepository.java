package com.martina.gestione_eventi.repositories;

import com.martina.gestione_eventi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByOrganizzatoreId(Long organizzatoreId);

    boolean existsByIdAndOrganizzatoreId(
            Long eventoId,
            Long organizzatoreId
    );
}