package com.martina.gestione_eventi.exceptions;

public class OperazioneNonConsentitaException extends RuntimeException {

    public OperazioneNonConsentitaException(String message) {
        super(message);
    }
}