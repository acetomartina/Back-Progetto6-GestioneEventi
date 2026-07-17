package com.martina.gestione_eventi.controllers;

import com.martina.gestione_eventi.dto.EventoRequest;
import com.martina.gestione_eventi.dto.EventoResponse;
import com.martina.gestione_eventi.services.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public ResponseEntity<List<EventoResponse>> trovaTutti() {

        return ResponseEntity.ok(
                eventoService.trovaTutti()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> trovaPerId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                eventoService.trovaPerId(id)
        );
    }

    @GetMapping("/miei")
    public ResponseEntity<List<EventoResponse>> trovaMieiEventi(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                eventoService.trovaEventiOrganizzatore(
                        authentication.getName()
                )
        );
    }

    @PostMapping
    public ResponseEntity<EventoResponse> creaEvento(
            @Valid @RequestBody EventoRequest request,
            Authentication authentication
    ) {

        EventoResponse response =
                eventoService.creaEvento(
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> modificaEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest request,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                eventoService.modificaEvento(
                        id,
                        request,
                        authentication.getName()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaEvento(
            @PathVariable Long id,
            Authentication authentication
    ) {

        eventoService.eliminaEvento(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}