package app;

import db.DatabaseManager;
import model.Member;
import model.MembershipType;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


//Die MemberManager-Klasse verwaltet die Mitglieder des Vereins

public class MemberManager {
    private final DatabaseManager datenbankverwaltung;
    private final List<MembershipType> types;
    private final Scanner eingabe;

    public MemberManager(DatabaseManager datenbankverwaltung) {
        this.datenbankverwaltung = datenbankverwaltung;
        //Lädt einmalig alle Mitgliedsarten aus der Datenbank
        this.types = datenbankverwaltung.getAllMembershipTypes();
        this.eingabe = new Scanner(System.in);
    }

    //Unendliche Schleife für das Konsolenmenü

    public void menu() {
        System.out.println("\n ===Willkommen in der Mitgliederverwaltung!!!===");
        while (true) {
            System.out.println("\n Bitte wählen sie eine Option:");
            System.out.println("1 = Mitglied hinzufügen");
            System.out.println("2 = Mitglied löschen");
            System.out.println("3 = Mitglied bearbeiten");
            System.out.println("4 = Mitglied suchen");
            System.out.println("5 = Alle Mitglieder+Daten anzeigen");
            System.out.println("0 = Beenden");

            System.out.print("Bitte wählen Sie eine Option: ");
            int auswahl;
            try {
                auswahl = Integer.parseInt(eingabe.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Bitte geben Sie eine Zahl ein.");
                continue;
            }

            switch (auswahl) {
                case 1 -> VerwAddMember();
                case 2 -> VerwDeleteMember();
                case 3 -> VerwUpdateMember();
                case 4 -> VerwSearchMember();
                case 5 -> VerwListAllMembers();
                case 0 -> { System.out.println("Programm wird beendet.");
                    eingabe.close(); // Scanner schließen
                    return; // Beendet die Schleife und das Programm
                }
                default -> System.out.println("Ungültige Auswahl, bitte versuchen Sie es erneut.");
            }
        }
    }


    // Methode zum Hinzufügen eines Mitglieds
    public void VerwAddMember() {
        System.out.println("\n===Mitglied hinzufügen===");
        try {
            System.out.print("Vorname: ");
            String vorname = eingabe.nextLine();
            System.out.print("Nachname: ");
            String nachname = eingabe.nextLine();
            System.out.print("Geburtsdatum (YYYY-MM-DD): ");
            LocalDate geburtsdatum = LocalDate.parse(eingabe.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
            System.out.print("Strasse: ");
            String strasse = eingabe.nextLine();
            System.out.print("PLZ: ");
            String plz = eingabe.nextLine();
            System.out.print("Ort: ");
            String ort = eingabe.nextLine();
            System.out.print("E-Mail: ");
            String email = eingabe.nextLine();
            System.out.print("Eintrittsdatum (JJJJ-MM-TT): ");
            LocalDate eintrittsdatum = LocalDate.parse(eingabe.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);

            //Anzeigen der verfügbaren Mitgliedsarten
            System.out.println("Verfügbare Mitgliedsarten:");
            for (MembershipType t : types) {
                System.out.printf("%d = %s%n", t.getMembershipTypeID(), t.getBezeichnung());
            }
            MembershipType mt = null;
            while (mt == null) {
                System.out.print("Bitte wählen Sie eine Mitgliedsart (ID eingeben): ");
                int mitgliedsartID;
                try {
                    mitgliedsartID = Integer.parseInt(eingabe.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige ID. Bitte geben Sie eine Zahl ein.");
                    continue;
                }
                for (MembershipType t : types) {
                    if (t.getMembershipTypeID() == mitgliedsartID) {
                        mt = t;
                        break;
                    }
                }
                if (mt == null) {
                    System.out.println("Ungültige Mitgliedsart, bitte versuchen sie es erneut.");
                }
            }

            //Erstellen des neuen Mitglieds
            Member m = new Member(vorname, nachname, geburtsdatum, strasse, plz, ort, email, mt, eintrittsdatum);

            //Hinzufügen des Mitglieds zur Datenbank
            int newID = datenbankverwaltung.addMember(m);

            if (newID > 0) {
                System.out.println("Mitglied erfolgreich hinzugefügt mit ID: " + newID);
            } else {
                System.out.println("Fehler beim Hinzufügen des Mitglieds.");
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der Eingabe: " + e.getMessage());
        }
    }

    //Methode zum Löschen eines Mitglieds
    public void VerwDeleteMember() {
        System.out.println("\n===Mitglied löschen===");
        System.out.print("Bitte geben Sie die ID des zu löschenden Mitglieds ein: ");
        int mitgliedID;
        try {
            mitgliedID = Integer.parseInt(eingabe.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ungültige ID. Bitte geben Sie eine Zahl ein.");
            return;
        }

        if (datenbankverwaltung.deleteMember(mitgliedID)) {
            System.out.println("Mitglied mit ID " + mitgliedID + " wurde erfolgreich gelöscht.");
        } else {
            System.out.println("Fehler beim Löschen des Mitglieds. Bitte überprüfen Sie die ID.");
        }
    }

    //Methode zum Bearbeiten eines Mitglieds
    public void VerwUpdateMember() {
        System.out.println("\n===Mitglied bearbeiten===");
        System.out.println("Hinweis: Wenn Sie die Eingabe leer lassen und einfach Enter drücken, bleibt der bisherige Wert erhalten.");
        System.out.print("Bitte geben Sie die ID des zu bearbeitenden Mitglieds ein: ");
        int mitgliedID;
        try {
            mitgliedID = Integer.parseInt(eingabe.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Ungültige ID. Bitte geben Sie eine Zahl ein.");
            return;
        }

        Member member = datenbankverwaltung.getMemberbyID(mitgliedID);
        if (member == null) {
            System.out.println("Mitglied mit ID " + mitgliedID + " nicht gefunden.");
            return;
        }

        //Aktuelle Daten anzeigen
        System.out.println("Aktuelle Daten des Mitglieds:");
        System.out.println(member);

        try {
            //Daten bearbeiten
            System.out.print("Neuer Vorname (aktuell: " + member.getVorname() + "): ");
            String vorname = eingabe.nextLine().trim();
            if (!vorname.isEmpty()) {
                member.setVorname(vorname);
            }

            System.out.print("Neuer Nachname (aktuell: " + member.getNachname() + "): ");
            String nachname = eingabe.nextLine().trim();
            if (!nachname.isEmpty()) {
                member.setNachname(nachname);
            }

            System.out.print("Neues Geburtsdatum (JJJJ-MM-TT/YYYY-MM-DD), aktuell: " + member.getGeburtsdatum() + "): ");
            String geburtsdatumInput = eingabe.nextLine().trim();
            if (!geburtsdatumInput.isEmpty()) {
                try {
                    LocalDate geburtsdatum = LocalDate.parse(geburtsdatumInput, DateTimeFormatter.ISO_LOCAL_DATE);
                    member.setGeburtsdatum(geburtsdatum);
                } catch (Exception e) {
                    System.out.println("Ungültiges Datumsformat, Geburtsdatum bleibt unverändert.");
                }
            }

            System.out.print("Neue Strasse (aktuell: " + member.getStrasse() + "): ");
            String strasse = eingabe.nextLine().trim();
            if (!strasse.isEmpty()) {
                member.setStrasse(strasse);
            }

            System.out.print("Neue PLZ (aktuell: " + member.getPlz() + "): ");
            String plz = eingabe.nextLine().trim();
            if (!plz.isEmpty()) {
                member.setPlz(plz);
            }

            System.out.print("Neuer Ort (aktuell: " + member.getOrt() + "): ");
            String ort = eingabe.nextLine().trim();
            if (!ort.isEmpty()) {
                member.setOrt(ort);
            }

            System.out.print("Neue E-Mail (aktuell: " + member.getEmail() + "): ");
            String email = eingabe.nextLine().trim();
            if (!email.isEmpty()) {
                member.setEmail(email);
            }

            System.out.print("Neues Eintrittsdatum (JJJJ-MM-TT/YYYY-MM-DD), aktuell: " + member.getEintrittsdatum() + "): ");
            String eintrittsdatumInput = eingabe.nextLine().trim();
            if (!eintrittsdatumInput.isEmpty()) {
                try {
                    LocalDate eintrittsdatum = LocalDate.parse(eintrittsdatumInput, DateTimeFormatter.ISO_LOCAL_DATE);
                    member.setEintrittsdatum(eintrittsdatum);
                } catch (Exception e) {
                    System.out.println("Ungültiges Datumsformat, Eintrittsdatum bleibt unverändert.");
                }
            }

            //Anzeigen der verfügbaren Mitgliedsarten
            System.out.println("Verfügbare Mitgliedsarten:");
            for (MembershipType t : types) {
                System.out.printf("%d = %s%n", t.getMembershipTypeID(), t.getBezeichnung());
            }

            System.out.print("Bitte wählen Sie eine Mitgliedsart (ID eingeben, aktuell: " + member.getMitgliedsart().getBezeichnung() + ", Enter für keine Änderung): ");
            String mitgliedsartInput = eingabe.nextLine().trim();
            if (!mitgliedsartInput.isEmpty()) {
                try {
                    int mitgliedsartID = Integer.parseInt(mitgliedsartInput);
                    MembershipType mt = null;
                    for (MembershipType t : types) {
                        if (t.getMembershipTypeID() == mitgliedsartID) {
                            mt = t;
                            break;
                        }
                    }
                    if (mt != null) {
                        member.setMitgliedsart(mt);
                    } else {
                        System.out.println("Ungültige Mitgliedsart, bleibt unverändert.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige ID, Mitgliedsart bleibt unverändert.");
                }
            }

            //Anzeigen des aktualisierten Mitglieds und Datenbankaktualisierung
            if (datenbankverwaltung.updateMember(member)) {
                System.out.println("Mitglied erfolgreich aktualisiert: " + member);
            } else {
                System.out.println("Fehler beim Aktualisieren des Mitglieds.");
            }
        } catch (Exception e) {
            System.out.println("Fehler bei der Eingabe: " + e.getMessage());
        }
    }

    // Methode zum Suchen eines Mitglieds
    public void VerwSearchMember() {
        System.out.println("\n===Mitglied suchen===");
        System.out.println("1 = Suche nach ID");
        System.out.println("2 = Suche nach Name");
        System.out.print("Bitte wählen sie eine Option: ");
        int suchOption;
        try {
            suchOption = Integer.parseInt(eingabe.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Option. Bitte geben Sie eine Zahl ein.");
            return;
        }
        if (suchOption == 1) {
            System.out.print("Bitte geben Sie die ID des zu suchenden Mitglieds ein: ");
            int mitgliedID;
            try {
                mitgliedID = Integer.parseInt(eingabe.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige ID. Bitte geben Sie eine Zahl ein.");
                return;
            }
            Member member = datenbankverwaltung.getMemberbyID(mitgliedID);
            if (member != null) {
                System.out.println("Mitglied gefunden: " + member);
            } else {
                System.out.println("Mitglied mit ID " + mitgliedID + " nicht gefunden.");
            }
        } else if (suchOption == 2) {
            System.out.print("Bitte geben Sie den Namen des zu suchenden Mitglieds ein: ");
            String name = eingabe.nextLine();
            List<Member> members = datenbankverwaltung.searchMembersByName(name);
            if (!members.isEmpty()) {
                System.out.println("Gefundene Mitglieder:");
                for (Member m : members) {
                    System.out.println(m);
                }
            } else {
                System.out.println("Keine Mitglieder mit dem Namen " + name + " gefunden.");
            }
        } else {
            System.out.println("Ungültige Option, bitte versuchen Sie es erneut.");
        }
    }

    // Methode zum Anzeigen aller Mitglieder
    public void VerwListAllMembers() {
        System.out.println("\n===Alle Mitglieder anzeigen===");
        List<Member> members = datenbankverwaltung.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("Keine Mitglieder gefunden.");
        } else {
            System.out.println("Liste aller Mitglieder:");
            for (Member m : members) {
                System.out.println(m);
            }
        }
    }
















}
