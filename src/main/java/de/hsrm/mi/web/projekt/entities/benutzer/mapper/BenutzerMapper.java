package de.hsrm.mi.web.projekt.entities.benutzer.mapper;

import org.mapstruct.Mapper;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerFormular;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

@Mapper(componentModel = "spring")
public interface BenutzerMapper {

    // BenutzerFormular aus Benutzer-Entitaet fuellen
    BenutzerFormular benutzerToBenutzerFormular(Benutzer benutzer);
    // Benutzer-Entitaet aus Formularinhalt fuellen
    Benutzer benutzerFormularToBenutzer(String loginName, BenutzerFormular formular);
}
