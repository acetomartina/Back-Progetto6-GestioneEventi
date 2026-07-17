package com.martina.gestione_eventi.exceptions;

public class EmailGiaRegistrataException extends RuntimeException {

    public EmailGiaRegistrataException(String message) {
        super(message);
    }
}