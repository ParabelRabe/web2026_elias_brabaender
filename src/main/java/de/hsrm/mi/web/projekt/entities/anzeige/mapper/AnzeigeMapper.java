package de.hsrm.mi.web.projekt.entities.anzeige.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import de.hsrm.mi.web.projekt.anzeige.api.dto.AnzeigeDTO;
import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeFormular;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;

@Mapper(componentModel = "spring")
public interface AnzeigeMapper {

    // BenutzerFormular aus Benutzer-Entitaet fuellen
    AnzeigeFormular anzeigeToAnzeigeFormular(Anzeige anzeige);
    // Benutzer-Entitaet aus Formularinhalt fuellen
    Anzeige anzeigeFormularToAnzeige(AnzeigeFormular formular);
    
    @Mapping(source = "titel", target = "title")
    @Mapping(source = "anbieterName", target = "anbieter.name")
    @Mapping(source = "anbieterAdresse", target = "anbieter.adresse")
    Anzeige anzeigeDTOToAnzeige (AnzeigeDTO dto);

    @Mapping(target = "verfuegbar", expression = "java(anzeige.getAnzahl() - anzeige.getBesteller().size())")
    @Mapping(source = "title", target = "titel")
    @Mapping(source = "anbieter.name", target = "anbieterName")
    @Mapping(source = "anbieter.adresse", target = "anbieterAdresse")
    AnzeigeDTO anzeigeToAnzeigeDTO (Anzeige anzeige);
    List<Anzeige> anzeigeDTOToAnzeigeList (List<AnzeigeDTO> dto);
    List<AnzeigeDTO> anzeigeToAnzeigeDTOList (List<Anzeige> anzeige);
}
