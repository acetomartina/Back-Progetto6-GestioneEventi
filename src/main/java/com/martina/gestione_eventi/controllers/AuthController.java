package com.martina.gestione_eventi.controllers;

import com.martina.gestione_eventi.dto.LoginRequest;
import com.martina.gestione_eventi.dto.LoginResponse;
import com.martina.gestione_eventi.dto.RegistrazioneUtenteRequest;
import com.martina.gestione_eventi.dto.RegistrazioneUtenteResponse;
import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.services.AutenticazioneService;
import com.martina.gestione_eventi.services.UtenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtenteService utenteService;
    private final AutenticazioneService autenticazioneService;

    @PostMapping("/register")
    public ResponseEntity<RegistrazioneUtenteResponse> registraUtente(
            @Valid @RequestBody RegistrazioneUtenteRequest request
    ) {

        Utente utenteRegistrato =
                utenteService.registraUtente(request);

        RegistrazioneUtenteResponse response =
                new RegistrazioneUtenteResponse(
                        utenteRegistrato.getId(),
                        utenteRegistrato.getNome(),
                        utenteRegistrato.getCognome(),
                        utenteRegistrato.getEmail(),
                        utenteRegistrato.getRuolo()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response =
                autenticazioneService.login(request);

        return ResponseEntity.ok(response);
    }
}