package com.martina.gestione_eventi.config;

import com.martina.gestione_eventi.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // La REST API utilizza JWT e non sessioni tradizionali
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // Registrazione e login pubblici
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        // Solo gli organizzatori possono vedere i propri eventi
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/eventi/miei"
                        ).hasRole("ORGANIZER")

                        // Solo gli organizzatori possono creare eventi
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/eventi"
                        ).hasRole("ORGANIZER")

                        // Solo gli organizzatori possono modificare eventi
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/eventi/**"
                        ).hasRole("ORGANIZER")

                        // Solo gli organizzatori possono eliminare eventi
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/eventi/**"
                        ).hasRole("ORGANIZER")

                        // Solo gli utenti possono gestire prenotazioni
                        .requestMatchers(
                                "/api/prenotazioni/**"
                        ).hasRole("USER")

                        // Gli utenti autenticati possono consultare gli eventi
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/eventi/**"
                        ).authenticated()

                        // Qualsiasi altro endpoint richiede autenticazione
                        .anyRequest().authenticated()
                )

                // Controlla il JWT prima del filtro standard di Spring
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}