package projet;
import java.util.Calendar;
public class Reservation implements Comparable {
    private int id;
    static int cd_compteur=0;
    private Client client;
    private Chambre chambre;
    private Calendar dateDebut;
    private Calendar dateFin;
    private String statut;

    public Reservation( Client client, Chambre chambre, Calendar db, Calendar df, String statut) throws DateSwitchException {
        this.id = ++Reservation.cd_compteur;
        this.client = client;
        this.chambre = chambre;
        this.statut = statut;
        if(db.after(df)) {
        	throw new DateSwitchException();
        }
        else { 
        	this.dateDebut = db;
            this.dateFin = df;
        	}
    }

	public int getId() {
        return id;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chambre getChambre() {
        return this.chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public Calendar getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Calendar dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Calendar getDateFin() {
        return dateFin;
    }

    public void setDateFin(Calendar dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }
// en attente annulée confirmée
    public void setStatut(String statut) {
        this.statut = statut;
    }

@Override
public int compareTo(Object o) {
	return this.getDateFin().after((Calendar)o)? 1:-1;
}

    
}