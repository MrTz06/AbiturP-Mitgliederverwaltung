package db;

import model.Member;
import model.MembershipType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Mitgliederverwaltung";
    private static final String DB_USER = "benutzername";
    private static final String DB_PASSWORD = "passwort";

    private Connection connection;




    //Verbindung

    //baut die Verbindung zur DB auf
    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Verbindung zur Datenbank hergestellt.");
        } catch (SQLException e) {
            System.err.println("Fehler beim Herstellen der Verbindung zur Datenbank: " + e.getMessage());
        }


    }

    //beendet die Verbindung zur DB

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Verbindung zur Datenbank geschlossen.");
            } catch (SQLException e) {
                System.err.println("Fehler beim Beenden der Verbindung zur Datenbank: " + e.getMessage());
            }
        }


    }



    //Mitgliedsarten
    //Lädt alle Mitgliedsarten aus der DB
public List<MembershipType> getAllMembershipTypes() {
        List<MembershipType> types = new ArrayList<>();
        String query = "SELECT * FROM Mitgliedsarten";             //SQL-Befehl

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int membershipTypeID = resultSet.getInt("membershipTypeID");
                String bezeichnung = resultSet.getString("bezeichnung");
                MembershipType membershipType = new MembershipType(membershipTypeID, bezeichnung);
                types.add(membershipType);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Mitgliedsarten: " + e.getMessage());
        }

        return types;
    }




    //Mitglieder
    //Fügt ein neues Mitglied in die DB ein, gibt die generierte ID zurück oder -1 bei Fehler
    public int addMember (Member m) {
        String sql= "INSERT INTO Mitglieder (vorname, nachname, geburtsdatum, straße, plz, ort, email, mitgliedsart, eintrittsdatum) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //Probiert die DAten des Mitglieds in die DB zu schreiben
            statement.setString(1, m.getVorname());
            statement.setString(2, m.getNachname());
            statement.setDate(3, Date.valueOf(m.getGeburtsdatum()));
            statement.setString(4, m.getStraße());
            statement.setString(5, m.getPlz());
            statement.setString(6, m.getOrt());
            statement.setString(7, m.getEmail());
            statement.setInt(8, m.getMitgliedsart().getMembershipTypeID());
            statement.setDate(9, Date.valueOf(m.getEintrittsdatum()));

            int rowsAffected = statement.executeUpdate();   // Führt das SQL-INSERT-Statement aus und gibt die Anzahl der betroffenen Zeilen zurück
            if (rowsAffected > 0) {                         //Prüft, ob mindestens eine Zeile betroffen ist
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {      //Falls ja, wird der Primärschlüssel (ID) des neuen Mitglieds abgerufen
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Hinzufügen des Mitglieds: " + e.getMessage());
        }
        return -1;  // Gibt -1 zurück, wenn ein Fehler aufgetreten ist
    }

    //Löscht ein Mitglied aus der DB (anhand der ID)
    public boolean deleteMember(int memberId) {
        String sql = "DELETE FROM Mitglieder WHERE mitgliedID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;                                                // Gibt true zurück, wenn das Mitglied erfolgreich gelöscht wurde
        } catch (SQLException e) {
            System.err.println("Fehler beim Löschen des Mitglieds: " + e.getMessage());
            return false;
        }
    }




    //Updaten eines Mitglieds anhand der ID

    public boolean updateMember (Member m) {
        String sql = "UPDATE Mitglieder SET " +
                "vorname = ?, nachname = ?, geburtsdatum = ?, straße = ?, plz = ?, ort = ?, email = ?, mitgliedsart = ?, eintrittsdatum = ? " +
                "WHERE mitgliedID = ?";

       try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, m.getVorname());
            statement.setString(2, m.getNachname());
            statement.setDate(3, Date.valueOf(m.getGeburtsdatum()));
            statement.setString(4, m.getStraße());
            statement.setString(5, m.getPlz());
            statement.setString(6, m.getOrt());
            statement.setString(7, m.getEmail());
            statement.setInt(8, m.getMitgliedsart().getMembershipTypeID());
            statement.setDate(9, Date.valueOf(m.getEintrittsdatum()));
            statement.setInt(10, m.getMitgliedID());                    //Fragt die ID des Mitglieds ab, das aktualisiert werden soll (Keine Änderung)

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;                                                // Gibt true zurück, wenn das Mitglied erfolgreich aktualisiert wurde
        } catch (SQLException e) {
            System.err.println("Fehler beim Aktualisieren des Mitglieds: " + e.getMessage());
            return false;                                                           //Gibt false zurück, wenn ein Fehler aufgetreten ist
        }

    }


    //Suche nach Mitgliedern anhand der ID, Ausgabe aller Daten + Join mit Mitgliedsarten







}












