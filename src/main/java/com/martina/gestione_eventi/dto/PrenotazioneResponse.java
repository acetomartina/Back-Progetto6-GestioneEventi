package com.martina.gestione_eventi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PrenotazioneResponse {

    private Long id;
    private LocalDateTime dataPrenotazione;

    private Long eventoId;
    private String titoloEvento;
    private LocalDateTime dataEvento;
    private String luogo;

    private Long utenteId;
    private String emailUtente;
}