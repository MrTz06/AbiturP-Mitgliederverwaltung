package model;

import java.time.LocalDate;


public class Member {

    //Attribute
    private final int mitgliedID;   //Primärschlüssel (nur lesbar)


    private String vorname;
    private String nachname;
    private LocalDate geburtsdatum;
    private String straße;
    private String plz;
    private String ort;
    private String email;
    private MembershipType mitgliedsart; //Bezeichnung für die Mitgliedsart (wird dem Nutzer anstelle der ID ausgegeben)
    private LocalDate eintrittsdatum;

    //Konstruktoren

    //Konstruktor für neue Mitglieder (ID noch unbekannt)

    public Member(String vorname, String nachname, LocalDate geburtsdatum,
                  String straße, String plz, String ort, String email,
                  MembershipType mitgliedsart, LocalDate eintrittsdatum) {


        this.mitgliedID = -10; // ID wird später durch DB gesetzt (-10 ist Platzhalter)


        //Alle Attribute werden auf null/leer geprüft (Nutzung von isBlank statt isEmpty, da isEmpty keine Leerzeichen erkennt)

        if (vorname == null || vorname.isBlank()) {
            throw new IllegalArgumentException("Die Vorname-Eingabe darf nicht leer sein");
        }
        this.vorname = vorname;

        if (nachname == null || nachname.isBlank()) {
            throw new IllegalArgumentException("Die Nachname-Eingabe darf nicht leer sein");
        }
        this.nachname = nachname;

        if (geburtsdatum == null) {
            throw new IllegalArgumentException("Das Geburtsdatum darf nicht leer sein");
        }
        this.geburtsdatum = geburtsdatum;

        if (straße == null || straße.isBlank()) {
            throw new IllegalArgumentException("Die Straße-Eingabe darf nicht leer sein");
        }
        this.straße = straße;

        if (plz == null || plz.isBlank()) {
            throw new IllegalArgumentException("Die PLZ-Eingabe darf nicht leer sein");
        }
        this.plz = plz;

        if (ort == null || ort.isBlank()) {
            throw new IllegalArgumentException("Die Ort-Eingabe darf nicht leer sein");
        }
        this.ort = ort;

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Die Email-Eingabe darf nicht leer sein");
        }
        this.email = email;

        if (mitgliedsart == null) {
            throw new IllegalArgumentException("Die Mitgliedsart-Eingabe darf nicht leer sein");
        }
        this.mitgliedsart = mitgliedsart;

        if (eintrittsdatum == null) {
            throw new IllegalArgumentException("Das Eintrittsdatum darf nicht leer sein");
        }
        this.eintrittsdatum = eintrittsdatum;
    }


    //Konstruktor für bestehende Mitglieder aus der DB (ID bekannt)
    public Member(int mitgliedID, String vorname, String nachname,
                  LocalDate geburtsdatum, String straße, String plz,
                  String ort, String email, MembershipType mitgliedsart,
                  LocalDate eintrittsdatum) {

        //Hier keine Prüfung, da die Daten in der Datenbank bei Erstellung bereits geprüft wurden
        this.mitgliedID = mitgliedID;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.straße = straße;
        this.plz = plz;
        this.ort = ort;
        this.email = email;
        this.mitgliedsart = mitgliedsart;
        this.eintrittsdatum = eintrittsdatum;
    }

    //Getter-Methoden

    public int getMitgliedID() {
        return mitgliedID;
    }
    public String getVorname() {
        return vorname;
    }
    public String getNachname() {
        return nachname;
    }
    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }
    public String getStraße() {
        return straße;
    }
    public String getPlz() {
        return plz;
    }
    public String getOrt() {
        return ort;
    }
    public String getEmail() {
        return email;
    }
    public MembershipType getMitgliedsart() {
        return mitgliedsart;
    }
    public LocalDate getEintrittsdatum() {
        return eintrittsdatum;
    }


    //Setter-Methoden

    public void setVorname(String vorname) {
        if (vorname == null || vorname.isBlank()) {
            throw new IllegalArgumentException("Die Vorname-Eingabe darf nicht leer sein");
        }
        this.vorname = vorname;
    }
    public void setNachname(String nachname) {
        if (nachname == null || nachname.isBlank()) {
            throw new IllegalArgumentException("Die Nachname-Eingabe darf nicht leer sein");
        }
        this.nachname = nachname;
    }
    public void setGeburtsdatum(LocalDate geburtsdatum) {
        if (geburtsdatum == null) {
            throw new IllegalArgumentException("Das Geburtsdatum darf nicht leer sein");
        }
        this.geburtsdatum = geburtsdatum;
    }
    public void setStraße(String straße) {
        if (straße == null || straße.isBlank()) {
            throw new IllegalArgumentException("Die Straße-Eingabe darf nicht leer sein");
        }
        this.straße = straße;
    }
    public void setPlz(String plz) {
        if (plz == null || plz.isBlank()) {
            throw new IllegalArgumentException("Die PLZ-Eingabe darf nicht leer sein");
        }
        this.plz = plz;
    }
    public void setOrt(String ort) {
        if (ort == null || ort.isBlank()) {
            throw new IllegalArgumentException("Die Ort-Eingabe darf nicht leer sein");
        }
        this.ort = ort;
    }
    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Die Email-Eingabe darf nicht leer sein");
        }
        this.email = email;
    }
    public void setMitgliedsart(MembershipType mitgliedsart) {
        if (mitgliedsart == null) {
            throw new IllegalArgumentException("Die Mitgliedsart-Eingabe darf nicht leer sein");
        }
        this.mitgliedsart = mitgliedsart;
    }
    public void setEintrittsdatum(LocalDate eintrittsdatum) {
        if (eintrittsdatum == null) {
            throw new IllegalArgumentException("Das Eintrittsdatum darf nicht leer sein");
        }
        this.eintrittsdatum = eintrittsdatum;
    }

    //toString-Methode
    @Override
    public String toString() {
        return "Mitglied{" +
                "mitgliedID=" + mitgliedID +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geburtsdatum=" + geburtsdatum +
                ", straße='" + straße + '\'' +
                ", plz='" + plz + '\'' +
                ", ort='" + ort + '\'' +
                ", email='" + email + '\'' +
                ", mitgliedsart='" + mitgliedsart.getBezeichnung() + '\'' +
                ", eintrittsdatum=" + eintrittsdatum +
                '}';
    }






}