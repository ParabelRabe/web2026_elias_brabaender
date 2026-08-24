package de.hsrm.mi.web.projekt.anzeige.api.dto;

import java.time.LocalDate;

public record AnzeigeDTO(
    Long id,
    String titel,
    String beschreibung,
    int preis,
    int anzahl,
    LocalDate ablaufdatum,
    int verfuegbar,
    String anbieterName,
    String anbieterAdresse)
    {};
