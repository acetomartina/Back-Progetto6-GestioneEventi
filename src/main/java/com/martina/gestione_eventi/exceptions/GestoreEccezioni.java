package com.martina.gestione_eventi.exceptions;

import com.martina.gestione_eventi.dto.ErroreResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestoreEccezioni {

    @ExceptionHandler(EmailGiaRegistrataException.class)
    public ResponseEntity<ErroreResponse> gestisciEmailGiaRegistrata(
            EmailGiaRegistrataException exception
    ) {

        ErroreResponse response = new ErroreResponse(
                HttpStatus.CONFLICT.value(),
                "Conflitto",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}