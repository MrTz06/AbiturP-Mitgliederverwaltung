package app;


import db.DatabaseManager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //1. Datenbankmanager erstellen
        DatabaseManager datenbankverwaltung = new DatabaseManager();

        //2. Datenbankverbindung herstellen
        datenbankverwaltung.connect();

        //3. Mitgliedsmanager erstellen
        MemberManager mitgliedsverwaltung = new MemberManager(datenbankverwaltung);

        //4. Menü aufrufen
        mitgliedsverwaltung.menu();

        //5. Datenbankverbindung schließen
        datenbankverwaltung.disconnect();
    }
}