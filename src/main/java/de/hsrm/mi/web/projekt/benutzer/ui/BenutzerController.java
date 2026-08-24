package de.hsrm.mi.web.projekt.benutzer.ui;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.hsrm.mi.web.projekt.MessageProvider;
import de.hsrm.mi.web.projekt.benutzer.services.BenutzerService;
import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import de.hsrm.mi.web.projekt.entities.benutzer.mapper.BenutzerMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin")
//@SessionAttributes("formularMap")
public class BenutzerController {

    private final MessageProvider messageProvider;
    Logger logger = LoggerFactory.getLogger(BenutzerController.class);
    private final BenutzerService benutzerService;
    private final BenutzerMapper benutzerMapper;
    private PasswordEncoder passwordEncoder;

    public BenutzerController(BenutzerService benutzerService, BenutzerMapper benutzerMapper, MessageProvider messageProvider, PasswordEncoder passwordEncoder){
        this.benutzerService = benutzerService;
        this.benutzerMapper = benutzerMapper;
        this.messageProvider = messageProvider;
        this.passwordEncoder = passwordEncoder;
    }
    /*
    @ModelAttribute("formularMap")
    public Map<String, BenutzerFormular> initializeFormularMap(){
        logger.info("formularMap wird erzeugt");
        return new HashMap<>();
    }
    */

    @GetMapping("/benutzer/{n}")
    //public String handle(@PathVariable("n") String name, @ModelAttribute("formularMap") Map<String, BenutzerFormular> fMap, Model m){
    public String handle(@PathVariable("n") String name, Model m, HttpSession session){
        Optional<Benutzer> nutzer = this.benutzerService.findBenutzerById(name);
        BenutzerFormular f;
        if(nutzer.isEmpty()){
            f = new BenutzerFormular();
            logger.info("Neuer benutzer angelegt: {}", name);
        }
        else{
            Benutzer ben = nutzer.get();
            f = this.benutzerMapper.benutzerToBenutzerFormular(ben);
            session.setAttribute(name, ben.getVersion());
            m.addAttribute("artikelList", ben.getBestellungen());
        }
        m.addAttribute("pathname", name);
        m.addAttribute("formular", f);
        logger.info("Model wurde mit daten von Benutzer {} gefüllt", name);
        return "benutzer/bearbeiten";
    }

    @PostMapping("/benutzer/{n}")
    //public String okhandle(@PathVariable("n") String pname, @Valid @ModelAttribute("formular") BenutzerFormular f, BindingResult bindingResult, @ModelAttribute("formularMap") Map<String, BenutzerFormular> fMap, Model m) {
    public String okhandle(@PathVariable("n") String pname, @Valid @ModelAttribute("formular") BenutzerFormular f, BindingResult bindingResult, Model m, HttpSession session, Locale locale) {
        m.addAttribute("pathname", pname);
        if (!f.getPasswort().equals(f.getPasswortWiederholung())){
            bindingResult.rejectValue("passwortWiederholung", "benutzer.fehler.passwortwiederholung", "PasswortWiederholung stimmt nicht mit Passwort überein");
        }

        if(bindingResult.hasErrors()){
            logger.info("BindingResultfehler");
            m.addAttribute("info", messageProvider.resolve("benutzer.fehler.speichern", locale));
            return "benutzer/bearbeiten";
        }

        try {
            Benutzer benutzer = this.benutzerMapper.benutzerFormularToBenutzer(pname, f);

            Long gemerkteVersion = (Long) session.getAttribute(pname);
            if (gemerkteVersion != null) {
                benutzer.setVersion(gemerkteVersion);
            }


            if(benutzer.getPasswort() == null || benutzer.getPasswort().equals("")){
                logger.info("Benutzer hat kein Passwort mitgegeben: {}",benutzer.getLoginName());
                Optional<Benutzer> nutzer = this.benutzerService.findBenutzerById(pname);
                Benutzer altbenutzer = nutzer.orElseThrow(() -> new BenutzerException("Benutzer existiert nicht"));
                if(altbenutzer.getPasswort() == null || altbenutzer.getPasswort().equals("")){
                    logger.info("Benutzer hat kein Passwort ERROR: {}",benutzer.getLoginName());
                    throw new BenutzerException("Kein Passwort angegeben");
                }
                else{
                    benutzer.setPasswort(altbenutzer.getPasswort());
                    logger.info("Benutzer hat altes Passwort genommen: {}",benutzer.getLoginName());
                }
            }
            else{
                benutzer.setPasswort(passwordEncoder.encode(benutzer.getPasswort()));
            }
            Optional<Benutzer> nutzer = this.benutzerService.findBenutzerById(pname);
            if(nutzer.isEmpty()){
                logger.info("Adresse von Nutzer " + benutzer.getLoginName() + " ist leer");
                String adresse = this.benutzerService.testeBenutzerAdresse(benutzer.getAdresse());
                if(adresse == null){
                    logger.info("Benutzer hat keine Adresse ERROR: {}",benutzer.getLoginName());
                    bindingResult.rejectValue("adresse", "benutzer.fehler.adresse", "Adresse ist ungültig");
                    throw new BenutzerException("Keine Adresse angegeben");
                }
                else{
                    logger.info("Adresse von Nutzer " + benutzer.getLoginName() + " ist gültig");
                    benutzer.setAdresse(adresse);
                }
            }
            logger.info("Adresse von Nutzer " + benutzer.getLoginName() + " ist vorhanden: " + benutzer.getAdresse());
            this.benutzerService.saveBenutzer(benutzer);
            session.removeAttribute(pname);
            logger.info("Benutzer saved: {}",benutzer.getLoginName());
            m.addAttribute("artikelList", benutzer.getBestellungen());
        } catch (BenutzerException e) {
            logger.error(e.getMessage());
            m.addAttribute("info", messageProvider.resolve("benutzer.fehler.speichern", locale));
            return "benutzer/bearbeiten";
        } catch (ObjectOptimisticLockingFailureException e) {
            logger.warn("Optimistic Locking Konflikt bei Benutzer: {}", pname);
            m.addAttribute("info", messageProvider.resolve("benutzer.fehler.optimSperren", locale));
            return "benutzer/bearbeiten";
            
        }
        m.addAttribute("formular", f);
        m.addAttribute("pathname", pname);
        return "redirect:/admin/benutzer";
    }

    @GetMapping("/benutzer")
    public String handleUserList(Model m){
        List<Benutzer> benutzer = (List<Benutzer>) this.benutzerService.findAllBenutzer();
        m.addAttribute("benutzerList", benutzer);
        return "benutzer/liste";
    }

    @GetMapping("/benutzer/{n}/delete")
    public String deleteUser(@PathVariable("n") String name, Model m){
        this.benutzerService.deleteBenutzerById(name);
        return "redirect:/admin/benutzer";
    }

    @GetMapping("/benutzer/{loginName}/hx/feld/{feldname}")
    public String handleClickOnNutzerAttribut(@PathVariable("loginName") String login, @PathVariable("feldname") String feld, Model m){
        Optional<Benutzer> nutzer = benutzerService.findBenutzerById(login);
        Benutzer b = nutzer.orElseThrow();
        if(feld.equals("name")){
            m.addAttribute("wert", b.getName());
        }
        else if(feld.equals("email")){
            m.addAttribute("wert", b.getEmail());
        }
        return "benutzer/thymeleaf/eingabefeld :: bearbeiten";
    }

    @PutMapping("/benutzer/{loginName}/hx/feld/{feldname}")
    public String handleEingabeBeiBenutzer(@PathVariable("loginName") String login, @PathVariable("feldname") String feld, @RequestParam("wert") String w, Locale locale, Model m){
        try{
            Benutzer b = this.benutzerService.aktualisiereBenutzerAttribut(login, feld, w);
            if(feld.equals("name")){
                m.addAttribute("wert", b.getName());
            }
            else if(feld.equals("email")){
                m.addAttribute("wert", b.getEmail());
            }
            return "benutzer/thymeleaf/eingabefeld :: ausgeben";
        }catch(BenutzerException e){
            m.addAttribute("fehler", e.getMessage());
            Optional<Benutzer> nutzer = benutzerService.findBenutzerById(login);
            Benutzer b = nutzer.orElseThrow();
            if(feld.equals("name")){
                m.addAttribute("wert", b.getName());
            }
            else if(feld.equals("email")){
                m.addAttribute("wert", b.getEmail());
            }
            logger.error(e.getMessage());
            m.addAttribute("loginName", login);
            m.addAttribute("feldname", feld);
            return "benutzer/thymeleaf/eingabefeld :: bearbeiten";
        }
    }


}
