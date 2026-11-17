package de.hsrm.mi.web.projekt.entities.anzeige.mapper;

import de.hsrm.mi.web.projekt.anzeige.api.dto.AnzeigeDTO;
import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeFormular;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-17T19:10:11+0100",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.4.1.jar, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class AnzeigeMapperImpl implements AnzeigeMapper {

    @Override
    public AnzeigeFormular anzeigeToAnzeigeFormular(Anzeige anzeige) {
        if ( anzeige == null ) {
            return null;
        }

        AnzeigeFormular anzeigeFormular = new AnzeigeFormular();

        anzeigeFormular.setTitle( anzeige.getTitle() );
        anzeigeFormular.setBeschreibung( anzeige.getBeschreibung() );
        anzeigeFormular.setPreis( anzeige.getPreis() );
        anzeigeFormular.setAnzahl( anzeige.getAnzahl() );
        anzeigeFormular.setAblaufdatum( anzeige.getAblaufdatum() );
        anzeigeFormular.setAnbieter( anzeige.getAnbieter() );
        Collection<Benutzer> collection = anzeige.getBesteller();
        if ( collection != null ) {
            anzeigeFormular.setBesteller( new ArrayList<Benutzer>( collection ) );
        }

        return anzeigeFormular;
    }

    @Override
    public Anzeige anzeigeFormularToAnzeige(AnzeigeFormular formular) {
        if ( formular == null ) {
            return null;
        }

        Anzeige anzeige = new Anzeige();

        anzeige.setAnbieter( formular.getAnbieter() );
        anzeige.setTitle( formular.getTitle() );
        anzeige.setBeschreibung( formular.getBeschreibung() );
        anzeige.setPreis( formular.getPreis() );
        anzeige.setAnzahl( formular.getAnzahl() );
        anzeige.setAblaufdatum( formular.getAblaufdatum() );
        if ( anzeige.getBesteller() != null ) {
            Collection<Benutzer> collection = formular.getBesteller();
            if ( collection != null ) {
                anzeige.getBesteller().addAll( collection );
            }
        }

        return anzeige;
    }

    @Override
    public Anzeige anzeigeDTOToAnzeige(AnzeigeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Anzeige anzeige = new Anzeige();

        anzeige.setAnbieter( anzeigeDTOToBenutzer( dto ) );
        anzeige.setTitle( dto.titel() );
        if ( dto.id() != null ) {
            anzeige.setId( dto.id() );
        }
        anzeige.setBeschreibung( dto.beschreibung() );
        anzeige.setPreis( dto.preis() );
        anzeige.setAnzahl( dto.anzahl() );
        anzeige.setAblaufdatum( dto.ablaufdatum() );

        return anzeige;
    }

    @Override
    public AnzeigeDTO anzeigeToAnzeigeDTO(Anzeige anzeige) {
        if ( anzeige == null ) {
            return null;
        }

        String titel = null;
        String anbieterName = null;
        String anbieterAdresse = null;
        Long id = null;
        String beschreibung = null;
        int preis = 0;
        int anzahl = 0;
        LocalDate ablaufdatum = null;

        titel = anzeige.getTitle();
        anbieterName = anzeigeAnbieterName( anzeige );
        anbieterAdresse = anzeigeAnbieterAdresse( anzeige );
        id = anzeige.getId();
        beschreibung = anzeige.getBeschreibung();
        preis = anzeige.getPreis();
        anzahl = anzeige.getAnzahl();
        ablaufdatum = anzeige.getAblaufdatum();

        int verfuegbar = anzeige.getAnzahl() - anzeige.getBesteller().size();

        AnzeigeDTO anzeigeDTO = new AnzeigeDTO( id, titel, beschreibung, preis, anzahl, ablaufdatum, verfuegbar, anbieterName, anbieterAdresse );

        return anzeigeDTO;
    }

    @Override
    public List<Anzeige> anzeigeDTOToAnzeigeList(List<AnzeigeDTO> dto) {
        if ( dto == null ) {
            return null;
        }

        List<Anzeige> list = new ArrayList<Anzeige>( dto.size() );
        for ( AnzeigeDTO anzeigeDTO : dto ) {
            list.add( anzeigeDTOToAnzeige( anzeigeDTO ) );
        }

        return list;
    }

    @Override
    public List<AnzeigeDTO> anzeigeToAnzeigeDTOList(List<Anzeige> anzeige) {
        if ( anzeige == null ) {
            return null;
        }

        List<AnzeigeDTO> list = new ArrayList<AnzeigeDTO>( anzeige.size() );
        for ( Anzeige anzeige1 : anzeige ) {
            list.add( anzeigeToAnzeigeDTO( anzeige1 ) );
        }

        return list;
    }

    protected Benutzer anzeigeDTOToBenutzer(AnzeigeDTO anzeigeDTO) {
        if ( anzeigeDTO == null ) {
            return null;
        }

        Benutzer benutzer = new Benutzer();

        benutzer.setName( anzeigeDTO.anbieterName() );
        benutzer.setAdresse( anzeigeDTO.anbieterAdresse() );

        return benutzer;
    }

    private String anzeigeAnbieterName(Anzeige anzeige) {
        Benutzer anbieter = anzeige.getAnbieter();
        if ( anbieter == null ) {
            return null;
        }
        return anbieter.getName();
    }

    private String anzeigeAnbieterAdresse(Anzeige anzeige) {
        Benutzer anbieter = anzeige.getAnbieter();
        if ( anbieter == null ) {
            return null;
        }
        return anbieter.getAdresse();
    }
}
