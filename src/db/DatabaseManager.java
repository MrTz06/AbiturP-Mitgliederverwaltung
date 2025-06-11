package db;

import model.Member;
import model.MembershipType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Diese Klasse verwaltet die Datenbankverbindung und führt CRUD-Operationen für Mitglieder und Mitgliedsarten durch

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Mitgliederverwaltung";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "21022006";

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
        String sql= "INSERT INTO Mitglieder (vorname, nachname, geburtsdatum, strasse, plz, ort, email, mitgliedsart, eintrittsdatum) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            //Setzt die Daten des Mitglieds in die vorbereiteten SQL-Statements ein
            statement.setString(1, m.getVorname());
            statement.setString(2, m.getNachname());
            statement.setDate(3, Date.valueOf(m.getGeburtsdatum()));
            statement.setString(4, m.getStrasse());
            statement.setString(5, m.getPlz());
            statement.setString(6, m.getOrt());
            statement.setString(7, m.getEmail());
            statement.setInt(8, m.getMitgliedsart().getMembershipTypeID());
            statement.setDate(9, Date.valueOf(m.getEintrittsdatum()));

            // Führt das SQL-INSERT-Statement aus und gibt die Anzahl der betroffenen Zeilen zurück
            int rowsAffected = statement.executeUpdate();
            //Prüft, ob mindestens eine Zeile betroffen ist
            if (rowsAffected > 0) {
                //Falls ja, wird der Primärschlüssel (ID) des neuen Mitglieds abgerufen
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
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
                "vorname = ?, nachname = ?, geburtsdatum = ?, strasse = ?, plz = ?, ort = ?, email = ?, mitgliedsart = ?, eintrittsdatum = ? " + "WHERE mitgliedID = ?";

       try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, m.getVorname());
            statement.setString(2, m.getNachname());
            statement.setDate(3, Date.valueOf(m.getGeburtsdatum()));
            statement.setString(4, m.getStrasse());
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

    public Member getMemberbyID (int memberID) {

        String sql = "SELECT m.*, mt.bezeichnung FROM Mitglieder m "
                + "JOIN Mitgliedsarten mt ON m.mitgliedsart = mt.membershipTypeID "
                + "WHERE m.mitgliedID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int mitgliedID = resultSet.getInt("mitgliedID");
                String vorname = resultSet.getString("vorname");
                String nachname = resultSet.getString("nachname");
                LocalDate geburtsdatum = resultSet.getDate("geburtsdatum").toLocalDate();
                String strasse = resultSet.getString("strasse");
                String plz = resultSet.getString("plz");
                String ort = resultSet.getString("ort");
                String email = resultSet.getString("email");
                MembershipType mitgliedsart = new MembershipType(
                        resultSet.getInt("mitgliedsart"),
                        resultSet.getString("bezeichnung")
                );
                LocalDate eintrittsdatum = resultSet.getDate("eintrittsdatum").toLocalDate();

                return new Member(mitgliedID, vorname, nachname, geburtsdatum, strasse, plz, ort, email, mitgliedsart, eintrittsdatum);
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden des Mitglieds: " + e.getMessage());
        }
        return null;  // Gibt null zurück, wenn kein Mitglied gefunden wurde
    }


    //Suche nach Mitgliedern anhand des Namens (Vorname oder Nachname), Ausgabe aller Daten + Join mit Mitgliedsarten
    public List<Member> searchMembersByName (String name) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.*, mt.bezeichnung FROM Mitglieder m " + "JOIN Mitgliedsarten mt ON m.mitgliedsart = mt.membershipTypeID " + "WHERE m.vorname LIKE ? OR m.nachname LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchPattern = "%" + name + "%";  // Platzhalter für die Suche
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int mitgliedID = resultSet.getInt("mitgliedID");
                String vorname = resultSet.getString("vorname");
                String nachname = resultSet.getString("nachname");
                LocalDate geburtsdatum = resultSet.getDate("geburtsdatum").toLocalDate();
                String strasse = resultSet.getString("strasse");
                String plz = resultSet.getString("plz");
                String ort = resultSet.getString("ort");
                String email = resultSet.getString("email");
                MembershipType mitgliedsart = new MembershipType(
                        resultSet.getInt("mitgliedsart"),
                        resultSet.getString("bezeichnung")
                );
                LocalDate eintrittsdatum = resultSet.getDate("eintrittsdatum").toLocalDate();

                members.add(new Member(mitgliedID, vorname, nachname, geburtsdatum, strasse, plz, ort, email, mitgliedsart, eintrittsdatum));
            }
        } catch (SQLException e) {
            System.err.println("Fehler bei der Suche nach Mitgliedern: " + e.getMessage());
        }
        return members;
    }

// Gibt alle Mitglieder mitsamt aller jeweiliger Daten + Join mit Mitgliedsarten

public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT m.*, mt.bezeichnung FROM Mitglieder m " + "JOIN Mitgliedsarten mt ON m.mitgliedsart = mt.membershipTypeID";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int mitgliedID = resultSet.getInt("mitgliedID");
                String vorname = resultSet.getString("vorname");
                String nachname = resultSet.getString("nachname");
                LocalDate geburtsdatum = resultSet.getDate("geburtsdatum").toLocalDate();
                String strasse = resultSet.getString("strasse");
                String plz = resultSet.getString("plz");
                String ort = resultSet.getString("ort");
                String email = resultSet.getString("email");
                MembershipType mitgliedsart = new MembershipType(
                        resultSet.getInt("mitgliedsart"),
                        resultSet.getString("bezeichnung")
                );
                LocalDate eintrittsdatum = resultSet.getDate("eintrittsdatum").toLocalDate();

                members.add(new Member(mitgliedID, vorname, nachname, geburtsdatum, strasse, plz, ort, email, mitgliedsart, eintrittsdatum));
            }
        } catch (SQLException e) {
            System.err.println("Fehler beim Laden der Mitglieder: " + e.getMessage());
        }
        return members;
    }


}
