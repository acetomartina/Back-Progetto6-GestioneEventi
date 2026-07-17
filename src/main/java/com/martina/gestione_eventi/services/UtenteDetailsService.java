package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.entities.Utente;
import com.martina.gestione_eventi.repositories.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtenteDetailsService implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        String emailNormalizzata = email
                .trim()
                .toLowerCase();

        Utente utente = utenteRepository
                .findByEmail(emailNormalizzata)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utente non trovato"
                        )
                );

        return User
                .withUsername(utente.getEmail())
                .password(utente.getPassword())
                .authorities(
                        "ROLE_" + utente.getRuolo().name()
                )
                .build();
    }
}