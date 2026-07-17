package com.martina.gestione_eventi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventoResponse {

    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataEvento;
    private String luogo;
    private Integer numeroPosti;
    private long postiDisponibili;
    private Long organizzatoreId;
    private String organizzatoreEmail;
}