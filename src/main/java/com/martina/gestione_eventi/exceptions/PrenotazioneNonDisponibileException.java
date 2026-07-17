package com.martina.gestione_eventi.exceptions;

public class PrenotazioneNonDisponibileException
        extends RuntimeException {

    public PrenotazioneNonDisponibileException(String message) {
        super(message);
    }
}