package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.dto.LoginRequest;
import com.martina.gestione_eventi.dto.LoginResponse;
import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.exceptions.CredenzialiNonValideException;
import com.martina.gestione_eventi.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticazioneService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        String emailNormalizzata = request.getEmail()
                .trim()
                .toLowerCase();

        Utente utente = utenteRepository
                .findByEmail(emailNormalizzata)
                .orElseThrow(() ->
                        new CredenzialiNonValideException(
                                "Email o password non valide"
                        )
                );

        boolean passwordCorretta = passwordEncoder.matches(
                request.getPassword(),
                utente.getPassword()
        );

        if (!passwordCorretta) {
            throw new CredenzialiNonValideException(
                    "Email o password non valide"
            );
        }

        String token = jwtService.generaToken(utente);

        return new LoginResponse(
                token,
                "Bearer",
                utente.getId(),
                utente.getNome(),
                utente.getCognome(),
                utente.getEmail(),
                utente.getRuolo()
        );
    }
}