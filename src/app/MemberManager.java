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
            int auswahl = eingabe.nextInt();

            switch (auswahl) {
                case 1 -> VerwAddMember();
                case 2 -> VerwDeleteMember();
                case 3 -> VerwUpdateMember();
                case 4 -> VerwSearchMember();
                case 5 -> VerwListAllMembers();
                case 0 -> { System.out.println("Programm wird beendet.");
                    return; // Beendet die Schleife und das Programm
                }
                default -> System.out.println("Ungültige Auswahl, bitte versuchen Sie es erneut.");
            }
        }
    }


    // Methode zum Hinzufügen eines Mitglieds
    public void VerwAddMember() {
        System.out.println("\n===Mitglied hinzufügen===");
        System.out.print("Vorname: ");
        String vorname = eingabe.next();
        System.out.print("Nachname: ");
        String nachname = eingabe.next();
        System.out.print("Geburtsdatum (YYYY-MM-DD): ");
        LocalDate geburtsdatum = LocalDate.parse(eingabe.next(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Straße: ");
        String straße = eingabe.next();
        System.out.print("PLZ: ");
        String plz = eingabe.next();
        System.out.print("Ort: ");
        String ort = eingabe.next();
        System.out.print("E-Mail: ");
        String email = eingabe.next();
        System.out.print("Eintrittsdatum (JJJJ-MM-TT): ");
        LocalDate eintrittsdatum = LocalDate.parse(eingabe.next(), DateTimeFormatter.ISO_LOCAL_DATE);


        //Anzeigen der verfügbaren Mitgliedsarten
        System.out.println("Verfügbare Mitgliedsarten:");
        for (MembershipType t : types) {
            System.out.printf("%d = %s%n", t.getMembershipTypeID(), t.getBezeichnung());
        }
        MembershipType mt = null;
        while (mt == null) {
            System.out.print("Bitte wählen Sie eine Mitgliedsart (ID eingeben): ");
            int mitgliedsartID = eingabe.nextInt();
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
        Member m = new Member(vorname, nachname, geburtsdatum, straße, plz, ort, email, mt, eintrittsdatum);

        //Hinzufügen des Mitglieds zur Datenbank
        int newID = datenbankverwaltung.addMember(m);

        if (newID > 0) {
            System.out.println("Mitglied erfolgreich hinzugefügt mit ID: " + newID);
        } else {
            System.out.println("Fehler beim Hinzufügen des Mitglieds.");
        }


    }

    //Methode zum Löschen eines Mitglieds
    public void VerwDeleteMember() {
        System.out.println("\n===Mitglied löschen===");
        System.out.print("Bitte geben Sie die ID des zu löschenden Mitglieds ein: ");
        int mitgliedID = eingabe.nextInt();

        if (datenbankverwaltung.deleteMember(mitgliedID)) {
            System.out.println("Mitglied mit ID " + mitgliedID + " wurde erfolgreich gelöscht.");
        } else {
            System.out.println("Fehler beim Löschen des Mitglieds. Bitte überprüfen Sie die ID.");
        }
    }

    //Methode zum Bearbeiten eines Mitglieds
    public void VerwUpdateMember() {
        System.out.println("\n===Mitglied bearbeiten===");
        System.out.print("Bitte geben Sie die ID des zu bearbeitenden Mitglieds ein: ");
        int mitgliedID = eingabe.nextInt();

        Member member = datenbankverwaltung.getMemberbyID(mitgliedID);
        if (member == null) {
            System.out.println("Mitglied mit ID " + mitgliedID + " nicht gefunden.");
            return;
        }

        //Aktuelle Daten anzeigen
        System.out.println("Aktuelle Daten des Mitglieds:");
        System.out.println(member);

        //Daten bearbeiten
        System.out.print("Neuer Vorname (aktuell: " + member.getVorname() + "): ");
        String vorname = eingabe.next();
        System.out.print("Neuer Nachname (aktuell: " + member.getNachname() + "): ");
        String nachname = eingabe.next();
        System.out.print("Neues Geburtsdatum (JJJJ-MM-TT/YYYY-MM-DD), aktuell: " + member.getGeburtsdatum() + "): ");
        LocalDate geburtsdatum = LocalDate.parse(eingabe.next(), DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.print("Neue Straße (aktuell: " + member.getStraße() + "): ");
        String straße = eingabe.next();
        System.out.print("Neue PLZ (aktuell: " + member.getPlz() + "): ");
        String plz = eingabe.next();
        System.out.print("Neuer Ort (aktuell: " + member.getOrt() + "): ");
        String ort = eingabe.next();
        System.out.print("Neue E-Mail (aktuell: " + member.getEmail() + "): ");
        String email = eingabe.next();
        System.out.print("Neues Eintrittsdatum (JJJJ-MM-TT/YYYY-MM-DD), aktuell: " + member.getEintrittsdatum() + "): ");
        LocalDate eintrittsdatum = LocalDate.parse(eingabe.next(), DateTimeFormatter.ISO_LOCAL_DATE);

        //Anzeigen der verfügbaren Mitgliedsarten
        System.out.println("Verfügbare Mitgliedsarten:");
        for (MembershipType t : types) {
            System.out.printf("%d = %s%n", t.getMembershipTypeID(), t.getBezeichnung());
        }

        MembershipType mt = null;
        while (mt == null) {
            System.out.print("Bitte wählen Sie eine Mitgliedsart (ID eingeben, aktuell: " + member.getMitgliedsart().getBezeichnung() + "): ");
            int mitgliedsartID = eingabe.nextInt();
            for (MembershipType t : types) {
                if (t.getMembershipTypeID() == mitgliedsartID) {
                    mt = t;
                    break;
                }
            }
        }
        //Anzeigen des aktualisierten Mitglieds und Datenbankaktualisierung
        Member updated = new Member(
                member.getMitgliedID(), vorname, nachname, geburtsdatum, straße, plz, ort, email, mt, eintrittsdatum
        );
        if (datenbankverwaltung.updateMember(updated)) {
            System.out.println("Mitglied erfolgreich aktualisiert: " + updated);
        } else {
            System.out.println("Fehler beim Aktualisieren des Mitglieds.");
        }
    }

    // Methode zum Suchen eines Mitglieds
    public void VerwSearchMember() {
        System.out.println("\n===Mitglied suchen===");
        System.out.println("1 = Suche nach ID");
        System.out.println("2 = Suche nach Name");
        System.out.print("Bitte wählen sie eine Option: ");
        int suchOption = eingabe.nextInt();
        if (suchOption == 1) {
            System.out.print("Bitte geben Sie die ID des zu suchenden Mitglieds ein: ");
            int mitgliedID = eingabe.nextInt();
            Member member = datenbankverwaltung.getMemberbyID(mitgliedID);
            if (member != null) {
                System.out.println("Mitglied gefunden: " + member);
            } else {
                System.out.println("Mitglied mit ID " + mitgliedID + " nicht gefunden.");
            }
        } else if (suchOption == 2) {
            System.out.print("Bitte geben Sie den Namen des zu suchenden Mitglieds ein: ");
            String name = eingabe.next();
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
