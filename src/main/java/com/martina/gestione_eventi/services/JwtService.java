package com.martina.gestione_eventi.services;

import com.martina.gestione_eventi.entities.Utente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Genera un token contenente email e ruolo dell'utente
    public String generaToken(Utente utente) {

        Date dataCreazione = new Date();

        Date dataScadenza = new Date(
                dataCreazione.getTime() + jwtExpiration
        );

        return Jwts.builder()
                .subject(utente.getEmail())
                .claim("ruolo", utente.getRuolo().name())
                .issuedAt(dataCreazione)
                .expiration(dataScadenza)
                .signWith(getSigningKey())
                .compact();
    }

    // Estrae l'email salvata come subject del token
    public String estraiEmail(String token) {
        return estraiClaims(token).getSubject();
    }

    // Controlla firma, formato e scadenza del token
    public boolean tokenValido(String token) {

        try {
            estraiClaims(token);
            return true;

        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    // Legge il contenuto del token dopo aver verificato la firma
    private Claims estraiClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Converte la chiave Base64 di env.properties in una chiave crittografica
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}