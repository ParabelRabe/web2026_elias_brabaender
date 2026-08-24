package de.hsrm.mi.web.projekt.anzeige.ui;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.format.annotation.DateTimeFormat;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AnzeigeFormular {
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
    private Benutzer anbieter;
    private Collection<Benutzer> besteller = new HashSet<Benutzer>();

    public AnzeigeFormular(){
        this.title = "";
        this.beschreibung = "";
        this.preis = 0;
        this.anzahl = 0;
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

    public Benutzer getAnbieter() {
        return anbieter;
    }

    public void setAnbieter(Benutzer anbieter) {
        this.anbieter = anbieter;
    }

    public Collection<Benutzer> getBesteller() {
        return besteller;
    }

    public void setBesteller(Collection<Benutzer> besteller) {
        this.besteller = besteller;
    }

    @Override
    public String toString() {
        return "BenutzerFormular [title=" + title + ", beschreibung=" + beschreibung + ", preis=" + preis + ", anzahl=" + anzahl
                + ", ablaufdatum=" + ablaufdatum + "]";
    }
}
