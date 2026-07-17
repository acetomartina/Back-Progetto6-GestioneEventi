package com.martina.gestione_eventi.controllers;

import com.martina.gestione_eventi.dto.PrenotazioneResponse;
import com.martina.gestione_eventi.services.PrenotazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    @PostMapping("/eventi/{eventoId}")
    public ResponseEntity<PrenotazioneResponse> prenotaEvento(
            @PathVariable Long eventoId,
            Authentication authentication
    ) {

        PrenotazioneResponse response =
                prenotazioneService.prenotaEvento(
                        eventoId,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/mie")
    public ResponseEntity<List<PrenotazioneResponse>>
    trovaMiePrenotazioni(Authentication authentication) {

        return ResponseEntity.ok(
                prenotazioneService.trovaMiePrenotazioni(
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> annullaPrenotazione(
            @PathVariable Long id,
            Authentication authentication
    ) {

        prenotazioneService.annullaPrenotazione(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}