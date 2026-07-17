package com.martina.gestione_eventi.repositories;

import com.martina.gestione_eventi.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrenotazioneRepository
        extends JpaRepository<Prenotazione, Long> {

    boolean existsByUtenteIdAndEventoId(
            Long utenteId,
            Long eventoId
    );

    long countByEventoId(Long eventoId);

    List<Prenotazione> findByUtenteId(Long utenteId);

    Optional<Prenotazione> findByIdAndUtenteId(
            Long prenotazioneId,
            Long utenteId
    );
}