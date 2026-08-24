package de.hsrm.mi.web.projekt.benutzer.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import de.hsrm.mi.web.geo.GeoAdresse;
import de.hsrm.mi.web.geo.GeoService;
import de.hsrm.mi.web.projekt.benutzer.ui.BenutzerException;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.BenutzerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
@Transactional
public class BenutzerServiceImp implements BenutzerService{

    private final BenutzerRepository benutzerRepository;
    private final Validator validator;
    @PersistenceContext
    private EntityManager entityManager;
    Logger logger = LoggerFactory.getLogger(BenutzerServiceImp.class);
    private final GeoService geoService;
    public BenutzerServiceImp(BenutzerRepository benutzerRepository, Validator validator, GeoService geoService){
        this.benutzerRepository = benutzerRepository;
        this.validator = validator;
        this.geoService = geoService;
    }

    @Override
    public Benutzer saveBenutzer(Benutzer b) {
        Set<ConstraintViolation<Benutzer>> violations = validator.validate(b);
        if (!violations.isEmpty()){
            if (entityManager.contains(b)) {
                entityManager.refresh(b);
            }
            throw new BenutzerException(violations.iterator().next().getMessage());
        }
        Benutzer benutzer = this.benutzerRepository.save(b);
        logger.info("Benutzer " + b.getLoginName() + "/" + b.getName() + " wird gespeichert");
        return benutzer;
    }

    @Override
    public Optional<Benutzer> findBenutzerById(String loginName) {
        Optional<Benutzer> b = this.benutzerRepository.findById(loginName);
        Set<ConstraintViolation<Optional<Benutzer>>> violations = validator.validate(b);
        if (!violations.isEmpty()){
            if (entityManager.contains(b)) {
                entityManager.refresh(b);
            }
            throw new BenutzerException(violations.iterator().next().getMessage());
        }
        logger.info("Benutzer wurde gefunden");
        return b;
    }

    @Override
    public Collection<Benutzer> findAllBenutzer() {
        Collection<Benutzer> b = this.benutzerRepository.findAll(Sort.by(Order.asc("loginName")));
        logger.info("Alle Benutzer wurden gefunden");
        return b;
    }

    @Override
    public void deleteBenutzerById(String loginName) {
        this.benutzerRepository.deleteById(loginName);
        logger.info("Benutzer mit " + loginName + " wird gelöscht");
    }

    @Override
    public Benutzer aktualisiereBenutzerAttribut(String loginName, String feldname, String wert) {
        Optional<Benutzer> b = this.findBenutzerById(loginName);
        Benutzer benutzer = b.orElseThrow(() -> new BenutzerException("Benutzer existiert nicht"));
        if(feldname.equals("email")){
            benutzer.setEmail(wert);
        }
        else if(feldname.equals("name")){
            benutzer.setName(wert);
        }
        benutzer = this.saveBenutzer(benutzer);
        return benutzer;
    }

    @Override
    public String testeBenutzerAdresse(String such) {
        List<GeoAdresse> adressen = geoService.findeAdressen(such);
        logger.info("Adresse {} wird getestet", such);
        if(!adressen.isEmpty()){
            String a = adressen.getFirst().display_name();
            logger.info("Adresse gefunden: {}", a);
            return a;
        }
        else{
            logger.info("Adresse nicht gefunden");
            return null;
        }
    }
    
}
