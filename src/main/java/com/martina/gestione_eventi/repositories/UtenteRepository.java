package com.martina.gestione_eventi.repositories;

import com.martina.gestione_eventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    // Controlla se un indirizzo email è già registrato
    boolean existsByEmail(String email);
}