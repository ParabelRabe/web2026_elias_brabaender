package de.hsrm.mi.web.projekt.entities.benutzer;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Benutzer implements Serializable{
    @Id
    private String loginName;
    @NotNull
    @Size(min=3, max=60)
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String adresse;
    @NotNull
    private String rolle;
    @NotNull
    private String passwort;
    @NotNull
    private boolean aktiviert;
    @Version
    long version;
    @OneToMany(mappedBy="anbieter", cascade = CascadeType.REMOVE)
    private Collection<Anzeige> anzeigen = new HashSet<Anzeige>();
    @ManyToMany(mappedBy="besteller")
    private Collection<Anzeige> bestellungen = new HashSet<Anzeige>();

    public Benutzer(){
        loginName = "";
        name = "";
        email ="";
        adresse ="";
        rolle ="";
        passwort="";
        aktiviert = false;
    }

    public void addBestellungen(Anzeige neueAnzeige){
        if (this.bestellungen != null) {
            this.bestellungen.size(); 
        }
        this.bestellungen.add(neueAnzeige);
        neueAnzeige.addBesteller(this);
    }

    public void removeBestellungen(Anzeige neueAnzeige){
        this.bestellungen.remove(neueAnzeige);
        neueAnzeige.removeBesteller(this);
    }

    public Collection<Anzeige> getBestellungen(){
        return this.bestellungen;
    }

    public void addAnzeige(Anzeige neueAnzeige){
        if (this.anzeigen != null) {
            this.anzeigen.size(); 
        }
        this.anzeigen.add(neueAnzeige);
        neueAnzeige.setAnbieter(this);
    }

    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getRolle() {
        return rolle;
    }
    public void setRolle(String rolle) {
        this.rolle = rolle;
    }
    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
    public boolean isAktiviert() {
        return aktiviert;
    }
    public void setAktiviert(boolean aktiviert) {
        this.aktiviert = aktiviert;
    }
    public long getVersion() {
        return version;
    }
    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "BenutzerFormular [loginName=" + loginName + ", name=" + name + ", email=" + email + ", adresse=" + adresse + ", rolle=" + rolle
                + ", passwort=" + passwort + ", passwortWiederholung=" + ", aktiviert="
                + aktiviert + "]";
    }
}
