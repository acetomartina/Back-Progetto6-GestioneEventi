package com.martina.gestione_eventi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class ErroreValidazioneResponse {

    private int status;
    private String errore;
    private Map<String, String> erroriCampi;
}