package com.martina.gestione_eventi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventoRequest {

    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(max = 100)
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(max = 1000)
    private String descrizione;

    @NotNull(message = "La data è obbligatoria")
    @Future(message = "La data dell'evento deve essere futura")
    private LocalDateTime dataEvento;

    @NotBlank(message = "Il luogo è obbligatorio")
    @Size(max = 150)
    private String luogo;

    @NotNull(message = "Il numero di posti è obbligatorio")
    @Min(value = 1, message = "Deve esserci almeno un posto")
    private Integer numeroPosti;
}