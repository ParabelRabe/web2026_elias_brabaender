package de.hsrm.mi.web.projekt.anzeige.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.hsrm.mi.web.projekt.anzeige.api.dto.AnzeigeDTO;
import de.hsrm.mi.web.projekt.anzeige.api.dto.AnzeigeNotFoundException;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeService;
import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.mapper.AnzeigeMapper;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;


@RestController
public class AnzeigeRestController {
    private AnzeigeService anzeigeService;
    private AnzeigeMapper anzeigeMapper;
    BenutzerService benutzerService;

    public AnzeigeRestController(AnzeigeService anzeigeService, AnzeigeMapper anzeigeMapper, BenutzerService benutzerService){
        this.anzeigeService = anzeigeService;
        this.anzeigeMapper = anzeigeMapper;
        this.benutzerService = benutzerService;
    }

    @GetMapping("/api/anzeige/{id}")
    public AnzeigeDTO getAnzeigeById(@PathVariable("id") String i) {
        long longId = Long.parseLong(i);
        Optional<Anzeige> a = anzeigeService.findAnzeigeById(longId);
        Anzeige an = a.orElseThrow(() -> new AnzeigeNotFoundException("Anzeige wurde nicht gefunden"));
        AnzeigeDTO adto = anzeigeMapper.anzeigeToAnzeigeDTO(an);
        return adto;
    }

    @GetMapping("/api/anzeige")
    public List<AnzeigeDTO> getMethodName() {
        List<Anzeige> anzeige = (List<Anzeige>) this.anzeigeService.findAllAnzeige();
        List<AnzeigeDTO> anzeigedto = this.anzeigeMapper.anzeigeToAnzeigeDTOList(anzeige);
        return anzeigedto;
    }

    @PostMapping("/api/anzeige/{id}/bestellen")
    public void bestellen( @PathVariable("id") String id, @RequestBody Map<String, String> payload) {
        
        long anzeigeId = Long.parseLong(id);
        String loginName = payload.get("loginName");
        
        if (loginName == null || loginName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LoginName fehlt");
        }

        try {
            Optional<Anzeige> a = anzeigeService.findAnzeigeById(anzeigeId);
            Anzeige an = a.orElseThrow(() -> new AnzeigeNotFoundException("Anzeige wurde nicht gefunden"));
            Optional<Benutzer> b = benutzerService.findBenutzerById(loginName);
            Benutzer bn = b.orElseThrow(() -> new AnzeigeNotFoundException("Benutzer wurde nicht gefunden"));
            anzeigeService.bestellen(an, bn);
            anzeigeService.saveAnzeige(an);
        } catch (AnzeigeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/api/anzeige/{id}/stornieren/{loginName}")
    public void stornieren( @PathVariable("id") String id, @PathVariable("loginName") String loginName) {
        
        long anzeigeId = Long.parseLong(id);
        
        if (loginName == null || loginName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "LoginName fehlt");
        }

        try {
            Optional<Anzeige> a = anzeigeService.findAnzeigeById(anzeigeId);
            Anzeige an = a.orElseThrow(() -> new AnzeigeNotFoundException("Anzeige wurde nicht gefunden"));
            Optional<Benutzer> b = benutzerService.findBenutzerById(loginName);
            Benutzer bn = b.orElseThrow(() -> new AnzeigeNotFoundException("Benutzer wurde nicht gefunden"));
            anzeigeService.stornieren(an, bn);
            anzeigeService.saveAnzeige(an);
        } catch (AnzeigeNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    
    
}
