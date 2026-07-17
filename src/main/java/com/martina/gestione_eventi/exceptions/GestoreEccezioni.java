package com.martina.gestione_eventi.exceptions;

import com.martina.gestione_eventi.dto.ErroreResponse;
import com.martina.gestione_eventi.dto.ErroreValidazioneResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GestoreEccezioni {

    // Gestisce il tentativo di registrazione con un'email già presente
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

    // Gestisce i dati della richiesta che non rispettano le validazioni
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroreValidazioneResponse> gestisciErroriValidazione(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> erroriCampi = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(errore -> erroriCampi.putIfAbsent(
                        errore.getField(),
                        errore.getDefaultMessage()
                ));

        ErroreValidazioneResponse response =
                new ErroreValidazioneResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Dati non validi",
                        erroriCampi
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Gestisce email o password non corrette durante il login
    @ExceptionHandler(CredenzialiNonValideException.class)
    public ResponseEntity<ErroreResponse> gestisciCredenzialiNonValide(
            CredenzialiNonValideException exception
    ) {

        ErroreResponse response = new ErroreResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Non autorizzato",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(RisorsaNonTrovataException.class)
    public ResponseEntity<ErroreResponse> gestisciRisorsaNonTrovata(
            RisorsaNonTrovataException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroreResponse(
                        404,
                        "Risorsa non trovata",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(OperazioneNonConsentitaException.class)
    public ResponseEntity<ErroreResponse> gestisciOperazioneNonConsentita(
            OperazioneNonConsentitaException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErroreResponse(
                        403,
                        "Operazione non consentita",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(PrenotazioneNonDisponibileException.class)
    public ResponseEntity<ErroreResponse>
    gestisciPrenotazioneNonDisponibile(
            PrenotazioneNonDisponibileException exception
    ) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErroreResponse(
                        409,
                        "Prenotazione non disponibile",
                        exception.getMessage()
                ));
    }
}