# Gestione Eventi API

REST API per la gestione di eventi e prenotazioni, sviluppata con **Spring Boot**, **Spring Security**, **JWT** e **PostgreSQL**.

Il progetto permette a due tipologie di utenti di interagire con la piattaforma:

- **ORGANIZER**: crea, modifica, elimina e consulta i propri eventi.
- **USER**: consulta gli eventi disponibili, effettua prenotazioni e gestisce le proprie prenotazioni.

> Progetto backend realizzato durante il percorso Full Stack Developer di EPICODE.

---

## Funzionalità principali

### Autenticazione e sicurezza

- Registrazione di nuovi utenti
- Login con generazione di token JWT
- Password codificate tramite `PasswordEncoder`
- Autenticazione stateless
- Autorizzazioni basate sui ruoli
- Validazione dei dati in ingresso
- Gestione centralizzata delle eccezioni
- Risposte HTTP personalizzate

### Gestione eventi

Gli utenti con ruolo `ORGANIZER` possono:

- creare un evento;
- visualizzare i propri eventi;
- modificare esclusivamente gli eventi creati da loro;
- eliminare esclusivamente i propri eventi;
- consultare il numero di posti disponibili.

### Gestione prenotazioni

Gli utenti con ruolo `USER` possono:

- visualizzare gli eventi;
- prenotare un evento;
- visualizzare le proprie prenotazioni;
- annullare una propria prenotazione.

Il sistema impedisce:

- prenotazioni duplicate;
- prenotazioni per eventi già iniziati;
- prenotazioni quando i posti sono esauriti;
- modifica di
