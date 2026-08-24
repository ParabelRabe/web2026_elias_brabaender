package de.hsrm.mi.web.projekt.anzeige.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.projekt.anzeige.ui.AnzeigeException;
import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.AnzeigeRepository;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.messaging.Event;
import de.hsrm.mi.web.projekt.messaging.FrontendNachrichtEvent;
import de.hsrm.mi.web.projekt.messaging.Operation;
import jakarta.transaction.Transactional;

@Service
public class AnzeigeServiceImp implements AnzeigeService{

    private final AnzeigeRepository anzeigeRepository;
    ApplicationEventPublisher publisher;
    Logger logger = LoggerFactory.getLogger(AnzeigeServiceImp.class);
    BenutzerService benutzerService;
    public AnzeigeServiceImp(AnzeigeRepository anzeigeRepository, BenutzerService benutzerService, ApplicationEventPublisher publisher){
        this.anzeigeRepository = anzeigeRepository;
        this.benutzerService = benutzerService;
        this.publisher = publisher;
    }

    @Override
    public Anzeige saveAnzeige(Anzeige b) {
        boolean ex = this.anzeigeRepository.existsById(b.getId());
        Anzeige anzeige = this.anzeigeRepository.save(b);
        logger.info("Anzeige " + b.getId() + "/" + b.getTitle() + " wird gespeichert");
        if (ex) {
            publisher.publishEvent(new FrontendNachrichtEvent(Event.ANZEIGE, b.getId(), Operation.UPDATE));
        }
        else{
            publisher.publishEvent(new FrontendNachrichtEvent(Event.ANZEIGE, b.getId(), Operation.CREATE));
        }
        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        return anzeige;
    }

    @Override
    public Optional<Anzeige> findAnzeigeById(long id) {
        Optional<Anzeige> b = this.anzeigeRepository.findById(id);
        logger.info("Anzeige wurde gefunden");
        return b;
    }

    @Override
    public Collection<Anzeige> findAllAnzeige() {
        Collection<Anzeige> b = this.anzeigeRepository.findAll(Sort.by(Order.asc("ablaufdatum")));
        logger.info("Alle Anzeige wurden gefunden");
        return b;
    }

    @Override
    public void deleteAnzeigeById(long id) {
        this.anzeigeRepository.deleteById(id);
        logger.info("Anzeige mit " + id + " wird gelöscht");
        publisher.publishEvent(new FrontendNachrichtEvent(Event.ANZEIGE, id, Operation.DELETE));
    }

    @Override
    public void bestellen(Anzeige a, Benutzer b) {
        if(a.getAnbieter().getName().equals(b.getName())){
            logger.info("Anbieter {} und Benutzer dürfen nicht gleich sein", b.getName());
            throw new AnzeigeException("Anbieter und Benutzer dürfen nicht gleich sein");
        }
        else if(a.getAnzahl() <= a.getBesteller().size()){
            logger.info("Angebot {} ist ausverkauft", a.getTitle());
            throw new AnzeigeException("Angebot ist ausverkauft");
        }
        else if(a.getBesteller().contains(b)) {
            logger.info("Benutzer {} ist bereits Besteller", b.getName());
            throw new AnzeigeException("Benutzer ist bereits Besteller");
        }
        else{
            b.addBestellungen(a);
            logger.info("Benutzer {} ist nun Besteller von {}", b.getName(), a.getTitle());
        }
    }

    @Override
    public void stornieren(Anzeige a, Benutzer b) {
        if(a.getBesteller().contains(b)){
            b.removeBestellungen(a);
            logger.info("Benutzer {} hat Angebot {} storniert", b.getName(), a.getTitle());
        }
        else{
            logger.info("Bestellung {} existiert nicht beim Benutzer {}",a.getTitle(), b.getName());
            throw new AnzeigeException("Bestellung existiert nicht beim Benutzer");
        }
    }

    @Override
    @Transactional
    public void verlosen(){
        Collection<Anzeige> anzeigen = this.findAllAnzeige();
        Collection<Benutzer> benutzer = benutzerService.findAllBenutzer();
        Random rand = new Random();
        for(Anzeige a : anzeigen){
            int r = rand.nextInt(benutzer.size());
            ArrayList<Benutzer> b = new ArrayList<>(benutzer);
            b.get(r).addAnzeige(a);
            logger.info("Benutzer {} wurde die Anzeige {} zugeordnet.", b.get(r).getLoginName(), a.getTitle());
        }
        for(Benutzer b : benutzer){
            for(int i = 0; i < 3;i++){
                int r = rand.nextInt(anzeigen.size());
                ArrayList<Anzeige> a = new ArrayList<>(anzeigen);
                try{
                    this.bestellen(a.get(r),b);
                    logger.info(b.getLoginName() + " wurde Besteller von " + a.get(r).getTitle());
                }catch(AnzeigeException e){
                    logger.error(e.getMessage());
                }
            }
        }
        logger.info("Verlosung beendet");
    }
    
}
