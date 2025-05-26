package model;

// Model-Klasse für Mitgliedsarten
public class MembershipType {

    private final int membershipTypeID;      // Primärschlüssel (nur lesbar) (wird von DB erstellt)
    private final String bezeichnung;        // Bezeichnung für die Mitgliedsart (wird dem Nutzer anstelle der ID ausgegeben)


    // Konstruktor für geladene Mitgliedsarten (werden bei programmstart geladen)

    public MembershipType(int membershipTypeID, String bezeichnung) {
        this.membershipTypeID = membershipTypeID;
        this.bezeichnung = bezeichnung;
    }

    //Gibt die IDs der Mitgliedsarten aus der DB zurück
    public int getMembershipTypeID() {
        return membershipTypeID;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }


    //toString Methode
    @Override
    public String toString() {
        return "membershipType{" +
                "membershipTypeID=" + membershipTypeID +
                ", bezeichnung='" + bezeichnung + '\'' +
                '}';
    }
}