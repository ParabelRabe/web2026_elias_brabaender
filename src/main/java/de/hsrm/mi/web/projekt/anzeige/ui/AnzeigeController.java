package de.hsrm.mi.web.projekt.anzeige.ui;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hsrm.mi.web.projekt.MessageProvider;
import de.hsrm.mi.web.projekt.anzeige.services.AnzeigeService;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import de.hsrm.mi.web.projekt.entities.anzeige.mapper.AnzeigeMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AnzeigeController {

    private final MessageProvider messageProvider;
    Logger logger = LoggerFactory.getLogger(AnzeigeController.class);
    private final AnzeigeService anzeigeservice;
    private final AnzeigeMapper anzeigeMapper;

    public AnzeigeController(AnzeigeService anzeigeservice, AnzeigeMapper anzeigeMapper, MessageProvider messageProvider){
        this.anzeigeservice = anzeigeservice;
        this.anzeigeMapper = anzeigeMapper;
        this.messageProvider = messageProvider;
    }

    @GetMapping("/anzeige/{n}")
    public String handle(@PathVariable("n") String id, Model m, HttpSession session){
        long longid = Long.parseLong(id);
        Optional<Anzeige> zeige = this.anzeigeservice.findAnzeigeById(longid);
        AnzeigeFormular f;
        if(longid == 0 || zeige.isEmpty()){
            f = new AnzeigeFormular();
            logger.info("Neue anzeige angelegt: {}", id);
        }
        else{
            Anzeige anzeige = zeige.get();
            logger.warn("Dies ist anbieter im get: " + anzeige.getAnbieter());
            f = this.anzeigeMapper.anzeigeToAnzeigeFormular(anzeige);
            logger.warn("Dies ist anbieter im formular: " + f.getAnbieter());
            session.setAttribute(id, anzeige.getVersion());
        }
        m.addAttribute("pathname", id);
        m.addAttribute("formular", f);

        logger.info("Model wurde mit daten von Anzeige {} gefüllt", id);
        return "anzeige/bearbeiten";
    }

    @PostMapping("/anzeige/{n}")
    public String okhandle(@PathVariable("n") String id, @Valid @ModelAttribute("formular") AnzeigeFormular f, BindingResult bindingResult, Model m, HttpSession session, Locale locale) {
        m.addAttribute("pathname", id);

        if(bindingResult.hasErrors()){
            logger.info("BindingResultfehler");
            m.addAttribute("info", messageProvider.resolve("anzeige.fehler.speichern", locale));
            return "anzeige/bearbeiten";
        }

        try {
            logger.warn("Dies ist anbieter im f: " + f.getAnbieter());
            Anzeige anzeige = this.anzeigeMapper.anzeigeFormularToAnzeige(f);
            logger.warn("Dies ist anbieter: " + anzeige.getAnbieter());
            long longId = Long.parseLong(id);
            Optional<Anzeige> zeige = this.anzeigeservice.findAnzeigeById(longId);
            Anzeige anzeigealt = zeige.get();
            anzeige.setAnbieter(anzeigealt.getAnbieter());
            logger.warn("Dies ist anbieter nun nach fix: " + anzeige.getAnbieter());

            if (longId > 0) {
                anzeige.setId(longId);
            }
            Long gemerkteVersion = (Long) session.getAttribute(id);
            if (gemerkteVersion != null) {
                anzeige.setVersion(gemerkteVersion);
            }
            this.anzeigeservice.saveAnzeige(anzeige);
            session.removeAttribute(id);
            logger.info("Anzeige saved: {}", anzeige.getId());
        } catch (AnzeigeException e) {
            logger.error(e.getMessage());
            m.addAttribute("info", messageProvider.resolve("anzeige.fehler.speichern", locale));
            return "anzeige/bearbeiten";
        } catch (ObjectOptimisticLockingFailureException e) {
            logger.warn("Optimistic Locking Konflikt bei Anzeige: {}", id);
            m.addAttribute("info", messageProvider.resolve("anzeige.fehler.optimSperren", locale));
            return "anzeige/bearbeiten";
            
        }
        m.addAttribute("formular", f);
        m.addAttribute("pathname", id);
        return "redirect:/admin/anzeige";
    }

    @GetMapping("/anzeige")
    public String handleAnzeigeList(Model m){
        List<Anzeige> anzeige = (List<Anzeige>) this.anzeigeservice.findAllAnzeige();
        m.addAttribute("anzeigeList", anzeige);
        return "anzeige/liste";
    }

    @GetMapping("/anzeige/{n}/delete")
    public String deleteAnzeige(@PathVariable("n") String id, Model m){
        long longid = Long.parseLong(id);
        this.anzeigeservice.deleteAnzeigeById(longid);
        return "redirect:/admin/anzeige";
    }

    @GetMapping("/anzeige/verlosung")
    public String verloseAnzeige(){
        this.anzeigeservice.verlosen();
        return "redirect:/admin/anzeige";
    }


}
