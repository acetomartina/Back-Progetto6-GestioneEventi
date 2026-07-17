package com.martina.gestione_eventi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErroreResponse {

    private int status;
    private String errore;
    private String messaggio;
}