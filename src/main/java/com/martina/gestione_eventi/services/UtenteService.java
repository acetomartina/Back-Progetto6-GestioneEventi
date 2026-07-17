package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.dto.RegistrazioneUtenteRequest;
import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.martina.gestione_eventi.exceptions.EmailGiaRegistrataException;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public Utente registraUtente(RegistrazioneUtenteRequest request) {

        // Uniformiamo l'email prima di salvarla
        String emailNormalizzata = request.getEmail()
                .trim()
                .toLowerCase();

        // Impediamo la registrazione della stessa email più volte
        if (utenteRepository.existsByEmail(emailNormalizzata)) {
            throw new EmailGiaRegistrataException("Email già registrata");
        }

        Utente utente = new Utente();

        utente.setNome(request.getNome().trim());
        utente.setCognome(request.getCognome().trim());
        utente.setEmail(emailNormalizzata);

        // La password viene codificata prima del salvataggio
        utente.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        utente.setRuolo(request.getRuolo());

        return utenteRepository.save(utente);
    }
}