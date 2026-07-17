package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.dto.EventoRequest;
import com.martina.gestione_eventi.dto.EventoResponse;
import com.martina.gestione_eventi.entities.Evento;
import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.enums.RuoloUtente;
import com.martina.gestione_eventi.exceptions.OperazioneNonConsentitaException;
import com.martina.gestione_eventi.exceptions.RisorsaNonTrovataException;
import com.martina.gestione_eventi.repositories.EventoRepository;
import com.martina.gestione_eventi.repositories.PrenotazioneRepository;
import com.martina.gestione_eventi.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final UtenteRepository utenteRepository;

    @Transactional
    public EventoResponse creaEvento(
            EventoRequest request,
            String email
    ) {

        Utente organizzatore = trovaUtente(email);
        verificaOrganizzatore(organizzatore);

        Evento evento = new Evento();

        aggiornaDatiEvento(evento, request);
        evento.setOrganizzatore(organizzatore);

        return convertiInResponse(
                eventoRepository.save(evento)
        );
    }

    public List<EventoResponse> trovaTutti() {

        return eventoRepository.findAll()
                .stream()
                .map(this::convertiInResponse)
                .toList();
    }

    public EventoResponse trovaPerId(Long id) {

        return convertiInResponse(
                trovaEvento(id)
        );
    }

    public List<EventoResponse> trovaEventiOrganizzatore(
            String email
    ) {

        Utente organizzatore = trovaUtente(email);
        verificaOrganizzatore(organizzatore);

        return eventoRepository
                .findByOrganizzatoreId(organizzatore.getId())
                .stream()
                .map(this::convertiInResponse)
                .toList();
    }

    @Transactional
    public EventoResponse modificaEvento(
            Long eventoId,
            EventoRequest request,
            String email
    ) {

        Utente organizzatore = trovaUtente(email);
        verificaOrganizzatore(organizzatore);

        Evento evento = trovaEvento(eventoId);
        verificaProprietario(evento, organizzatore);

        long prenotazioni =
                prenotazioneRepository.countByEventoId(eventoId);

        if (request.getNumeroPosti() < prenotazioni) {
            throw new OperazioneNonConsentitaException(
                    "Il numero di posti non può essere inferiore alle prenotazioni esistenti"
            );
        }

        aggiornaDatiEvento(evento, request);

        return convertiInResponse(
                eventoRepository.save(evento)
        );
    }

    @Transactional
    public void eliminaEvento(
            Long eventoId,
            String email
    ) {

        Utente organizzatore = trovaUtente(email);
        verificaOrganizzatore(organizzatore);

        Evento evento = trovaEvento(eventoId);
        verificaProprietario(evento, organizzatore);

        long prenotazioni =
                prenotazioneRepository.countByEventoId(eventoId);

        if (prenotazioni > 0) {
            throw new OperazioneNonConsentitaException(
                    "Non è possibile eliminare un evento con prenotazioni attive"
            );
        }

        eventoRepository.delete(evento);
    }

    private void aggiornaDatiEvento(
            Evento evento,
            EventoRequest request
    ) {

        evento.setTitolo(request.getTitolo().trim());
        evento.setDescrizione(request.getDescrizione().trim());
        evento.setDataEvento(request.getDataEvento());
        evento.setLuogo(request.getLuogo().trim());
        evento.setNumeroPosti(request.getNumeroPosti());
    }

    private Utente trovaUtente(String email) {

        return utenteRepository.findByEmail(
                email.trim().toLowerCase()
        ).orElseThrow(() ->
                new RisorsaNonTrovataException(
                        "Utente non trovato"
                )
        );
    }

    private Evento trovaEvento(Long id) {

        return eventoRepository.findById(id)
                .orElseThrow(() ->
                        new RisorsaNonTrovataException(
                                "Evento non trovato"
                        )
                );
    }

    private void verificaOrganizzatore(Utente utente) {

        if (utente.getRuolo() != RuoloUtente.ORGANIZER) {
            throw new OperazioneNonConsentitaException(
                    "Solo gli organizzatori possono gestire gli eventi"
            );
        }
    }

    private void verificaProprietario(
            Evento evento,
            Utente organizzatore
    ) {

        if (!evento.getOrganizzatore()
                .getId()
                .equals(organizzatore.getId())) {

            throw new OperazioneNonConsentitaException(
                    "Puoi modificare soltanto i tuoi eventi"
            );
        }
    }

    private EventoResponse convertiInResponse(Evento evento) {

        long prenotazioni =
                prenotazioneRepository.countByEventoId(
                        evento.getId()
                );

        long postiDisponibili = Math.max(
                0,
                evento.getNumeroPosti() - prenotazioni
        );

        return new EventoResponse(
                evento.getId(),
                evento.getTitolo(),
                evento.getDescrizione(),
                evento.getDataEvento(),
                evento.getLuogo(),
                evento.getNumeroPosti(),
                postiDisponibili,
                evento.getOrganizzatore().getId(),
                evento.getOrganizzatore().getEmail()
        );
    }
}