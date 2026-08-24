package de.hsrm.mi.web.projekt.anzeige.services;

import java.util.Collection;
import java.util.Optional;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;

public interface AnzeigeService {
    Anzeige saveAnzeige(Anzeige anzeige);
    Optional<Anzeige> findAnzeigeById(long id);
    Collection<Anzeige> findAllAnzeige();
    void deleteAnzeigeById(long id);
    void bestellen(Anzeige a, Benutzer b);
    void stornieren(Anzeige a, Benutzer b);
    void verlosen();
}
