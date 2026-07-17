package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.dto.PrenotazioneResponse;
import com.martina.gestione_eventi.entities.Evento;
import com.martina.gestione_eventi.entities.Prenotazione;
import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.enums.RuoloUtente;
import com.martina.gestione_eventi.exceptions.OperazioneNonConsentitaException;
import com.martina.gestione_eventi.exceptions.PrenotazioneNonDisponibileException;
import com.martina.gestione_eventi.exceptions.RisorsaNonTrovataException;
import com.martina.gestione_eventi.repositories.EventoRepository;
import com.martina.gestione_eventi.repositories.PrenotazioneRepository;
import com.martina.gestione_eventi.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    @Transactional
    public PrenotazioneResponse prenotaEvento(
            Long eventoId,
            String email
    ) {

        Utente utente = trovaUtente(email);
        verificaUtenteNormale(utente);

        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() ->
                        new RisorsaNonTrovataException(
                                "Evento non trovato"
                        )
                );

        if (!evento.getDataEvento().isAfter(LocalDateTime.now())) {
            throw new PrenotazioneNonDisponibileException(
                    "Non è possibile prenotare un evento già iniziato"
            );
        }

        boolean giaPrenotato =
                prenotazioneRepository
                        .existsByUtenteIdAndEventoId(
                                utente.getId(),
                                eventoId
                        );

        if (giaPrenotato) {
            throw new PrenotazioneNonDisponibileException(
                    "Hai già prenotato questo evento"
            );
        }

        long numeroPrenotazioni =
                prenotazioneRepository.countByEventoId(eventoId);

        if (numeroPrenotazioni >= evento.getNumeroPosti()) {
            throw new PrenotazioneNonDisponibileException(
                    "Non ci sono più posti disponibili"
            );
        }

        Prenotazione prenotazione = new Prenotazione();

        prenotazione.setDataPrenotazione(LocalDateTime.now());
        prenotazione.setUtente(utente);
        prenotazione.setEvento(evento);

        return convertiInResponse(
                prenotazioneRepository.save(prenotazione)
        );
    }

    public List<PrenotazioneResponse> trovaMiePrenotazioni(
            String email
    ) {

        Utente utente = trovaUtente(email);
        verificaUtenteNormale(utente);

        return prenotazioneRepository
                .findByUtenteId(utente.getId())
                .stream()
                .map(this::convertiInResponse)
                .toList();
    }

    @Transactional
    public void annullaPrenotazione(
            Long prenotazioneId,
            String email
    ) {

        Utente utente = trovaUtente(email);
        verificaUtenteNormale(utente);

        Prenotazione prenotazione =
                prenotazioneRepository
                        .findByIdAndUtenteId(
                                prenotazioneId,
                                utente.getId()
                        )
                        .orElseThrow(() ->
                                new RisorsaNonTrovataException(
                                        "Prenotazione non trovata"
                                )
                        );

        prenotazioneRepository.delete(prenotazione);
    }

    private Utente trovaUtente(String email) {

        return utenteRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                        new RisorsaNonTrovataException(
                                "Utente non trovato"
                        )
                );
    }

    private void verificaUtenteNormale(Utente utente) {

        if (utente.getRuolo() != RuoloUtente.USER) {
            throw new OperazioneNonConsentitaException(
                    "Solo gli utenti possono prenotare gli eventi"
            );
        }
    }

    private PrenotazioneResponse convertiInResponse(
            Prenotazione prenotazione
    ) {

        Evento evento = prenotazione.getEvento();
        Utente utente = prenotazione.getUtente();

        return new PrenotazioneResponse(
                prenotazione.getId(),
                prenotazione.getDataPrenotazione(),
                evento.getId(),
                evento.getTitolo(),
                evento.getDataEvento(),
                evento.getLuogo(),
                utente.getId(),
                utente.getEmail()
        );
    }
}