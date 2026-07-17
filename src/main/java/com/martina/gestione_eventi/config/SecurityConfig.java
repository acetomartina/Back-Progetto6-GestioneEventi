package com.martina.gestione_eventi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // Disabilitato perché stiamo costruendo una REST API
                // che utilizzerà l'autenticazione tramite JWT
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // La registrazione deve essere accessibile
                        // anche agli utenti non autenticati
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register"
                        ).permitAll()

                        // Tutti gli altri endpoint richiedono autenticazione
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}