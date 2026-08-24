package de.hsrm.mi.web.projekt.entities.anzeige;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.format.annotation.DateTimeFormat;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Anzeige implements Serializable{
    @Id
    @GeneratedValue
    private long id;
    @Version
    private long version;
    @NotNull @NotBlank @Size(min=3, max=40)
    private String title;
    @NotNull @NotBlank @Size(min=17)
    private String beschreibung;
    @Min(0)
    private int preis;
    @Min(0)
    private int anzahl;
    @Future
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ablaufdatum;
    @ManyToOne
    private Benutzer anbieter;
    @ManyToMany
    private Collection<Benutzer> besteller = new HashSet<Benutzer>();

    public Anzeige(){
        this.title = "";
        this.beschreibung = "";
        this.preis = 0;
        this.anzahl = 0;
    }

    public void addBesteller(Benutzer benutzer) {
        if (this.besteller != null) {
            this.besteller.size(); 
        }
        this.besteller.add(benutzer);
        
    }

    public void removeBesteller(Benutzer benutzer) {
        this.besteller.remove(benutzer);
    }

    public Collection<Benutzer> getBesteller(){
        return besteller;
    }

    public void setAnbieter(Benutzer anbieter){
        this.anbieter = anbieter;
    }

    public Benutzer getAnbieter(){
        return anbieter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getPreis() {
        return preis;
    }

    public void setPreis(int preis) {
        this.preis = preis;
    }

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public LocalDate getAblaufdatum() {
        return ablaufdatum;
    }

    public void setAblaufdatum(LocalDate ablaufdatum) {
        this.ablaufdatum = ablaufdatum;
    }

    @Override
    public String toString(){
        return "Anzeige [title=" + title + 
                        ", beschreibung=" + beschreibung + 
                        ", preis=" + preis +
                        ", anzahl=" + anzahl +
                        ", ablaufdatum=" + ablaufdatum + "]";
    }
}
