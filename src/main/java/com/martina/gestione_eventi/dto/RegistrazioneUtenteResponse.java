package com.martina.gestione_eventi.dto;

import com.martina.gestione_eventi.enums.RuoloUtente;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrazioneUtenteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private RuoloUtente ruolo;
}