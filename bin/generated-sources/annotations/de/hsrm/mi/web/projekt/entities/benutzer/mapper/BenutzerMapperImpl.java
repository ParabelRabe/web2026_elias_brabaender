package de.hsrm.mi.web.projekt.entities.benutzer.mapper;

import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerFormular;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-11-17T16:53:59+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class BenutzerMapperImpl implements BenutzerMapper {

    @Override
    public BenutzerFormular benutzerToBenutzerFormular(Benutzer benutzer) {
        if ( benutzer == null ) {
            return null;
        }

        BenutzerFormular benutzerFormular = new BenutzerFormular();

        benutzerFormular.setName( benutzer.getName() );
        benutzerFormular.setEmail( benutzer.getEmail() );
        benutzerFormular.setAdresse( benutzer.getAdresse() );
        benutzerFormular.setRolle( benutzer.getRolle() );
        benutzerFormular.setPasswort( benutzer.getPasswort() );
        benutzerFormular.setAktiviert( benutzer.isAktiviert() );

        return benutzerFormular;
    }

    @Override
    public Benutzer benutzerFormularToBenutzer(String loginName, BenutzerFormular formular) {
        if ( loginName == null && formular == null ) {
            return null;
        }

        Benutzer benutzer = new Benutzer();

        if ( formular != null ) {
            benutzer.setName( formular.getName() );
            benutzer.setEmail( formular.getEmail() );
            benutzer.setAdresse( formular.getAdresse() );
            benutzer.setRolle( formular.getRolle() );
            benutzer.setPasswort( formular.getPasswort() );
            benutzer.setAktiviert( formular.isAktiviert() );
        }
        benutzer.setLoginName( loginName );

        return benutzer;
    }
}
