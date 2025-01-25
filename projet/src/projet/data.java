package projet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;

public class data {
	private Connection connexion() {
		String url="jdbc:mysql://localhost:3306/Hotel";
		Connection connexion=null;
		try {
			connexion=DriverManager.getConnection(url,"root","root");
			}
		catch(SQLException e) {
			System.out.print("error de connexion");
		}
		return connexion;
	}
	public  ArrayList<Client> clients() throws SQLException{
		Connection cx=connexion();
		Statement st= cx.createStatement();
		String requete="select * from Client;";
		ResultSet resultat=st.executeQuery(requete);
		ArrayList<Client> listc=new ArrayList<Client>();
		while(resultat.next()) {
			int id=resultat.getInt("id");
			String nom=resultat.getString("nom");
			String email=resultat.getString("email");
			String tele=resultat.getString("telephone");
			Client ch=new Client(id,nom,email,tele);
			listc.add(ch);
		}
		return listc;
	}
	public  ArrayList<Chambre> chambres() throws SQLException{
		Connection cx=connexion();
		Statement st= cx.createStatement();
		String requete="select * from Chambre;";
		ResultSet resultat=st.executeQuery(requete);
		ArrayList<Chambre> listc=new ArrayList<Chambre>();
		while(resultat.next()) {
			int i=resultat.getInt("id");
			String type=resultat.getString("type");
			Double p=(double) resultat.getInt("prix");
			String status=resultat.getString("statut");
			Chambre ch=new Chambre(i,type,p,status);
			listc.add(ch);
		}
		return listc;
	}
	public  ArrayList<Reservation> reservations() throws SQLException, DateSwitchException{
		 Connection cx=connexion();
		 String query = "SELECT * FROM Reservation";
	     ArrayList<Reservation> reservations = new ArrayList<>();

	        try (
	             Statement st = cx.createStatement();
	             ResultSet rs = st.executeQuery(query)) {

	            ArrayList<Client> clients = clients();
	            ArrayList<Chambre> chambres = chambres();

	            while (rs.next()) {
	                int id = rs.getInt("id");
	                int clientId = rs.getInt("client_id");
	                int chambreId = rs.getInt("chambre_id");
	                Date dateDebut = rs.getDate("date_debut");
	                Date dateFin = rs.getDate("date_fin");
	                String statut = rs.getString("statut");

	                Client client = clients.stream().filter(c -> c.getId() == clientId).findFirst().orElse(null);
	                Chambre chambre = chambres.stream().filter(ch -> ch.getId() == chambreId).findFirst().orElse(null);

	                if (client != null && chambre != null) {
	                    Calendar debut = new GregorianCalendar();
	                    debut.setTime(dateDebut);
	                    Calendar fin = new GregorianCalendar();
	                    fin.setTime(dateFin);

	                    reservations.add(new Reservation(client, chambre, debut, fin, statut));
	                }
	            }
	        }
	        return reservations;
	    }
    public  ArrayList<Paiement> paiments() throws SQLException, DateSwitchException{
	Connection cx=connexion();
	Statement st= cx.createStatement();
	String requete="select * from Chambre;";
	ResultSet resultat=st.executeQuery(requete);
	ArrayList<Paiement> listp=new ArrayList<Paiement>();
	while(resultat.next()) {
		int i=resultat.getInt("id");
		int ir=resultat.getInt("reservation_id");
		Double m=(double) resultat.getInt("montant");
		String methode=resultat.getString("methode");
		GregorianCalendar d=new GregorianCalendar() ;
		d.setTime(resultat.getDate("date_paiement"));
		String status=resultat.getString("statut");
		Reservation res=reservations().get(ir);
		Paiement p=new Paiement(res,m,methode,status,d);
		listp.add(p);
	}
	return listp;
}
    public int addClientIfNotExists(String name, String email, String telephone) {
        Connection cx = connexion();
        String checkSql = "SELECT id FROM Client WHERE email = ?";  
        String insertSql = "INSERT INTO Client (nom, email, telephone) VALUES (?, ?, ?)";  

        try (
            PreparedStatement checkStmt = cx.prepareStatement(checkSql);
            PreparedStatement insertStmt = cx.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS) // Pour récupérer l'ID généré
        ) {
            
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();
            int id=0;

            if (rs.next()) {
                id=rs.getInt("id");
                return rs.getInt("id");
            }
            

       
            insertStmt.setString(1, name);
            insertStmt.setString(2, email);
            insertStmt.setString(3, telephone);
            insertStmt.executeUpdate();

    
            ResultSet generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

            return -1;  
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  
        }
    }

    public String updateChambre(Chambre ch ,String stat) {
        Connection cx = connexion();
        String req = "UPDATE Chambre SET statut = ? WHERE id = ?";
        try (PreparedStatement st = cx.prepareStatement(req)) {
      
            st.setString(1, stat);  
            st.setInt(2, ch.getId());    
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                return "Chambre mise à jour avec succès.";
            } else {
                return "Aucune chambre trouvée avec cet ID.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors de la mise à jour de la chambre : " + e.getMessage();
        }
    }

    public String reserver(Client cl, Chambre ch, Calendar debut, Calendar fin, String stat) throws ChambreIndisponibleException, SQLException {
        if (!ch.getStatut().equals("disponible")) {
            throw new ChambreIndisponibleException();
        } else {
            Connection cx = connexion();
            int clientS = addClientIfNotExists(cl.getNom(), cl.getEmail(), cl.getTelephone());
            cl.setId(clientS);
            System.out.println(clientS);
            Reservation res;
            try {
                res = new Reservation(cl, ch, debut, fin, stat);
                reservations().add(res);
                String req = "INSERT INTO Reservation (client_id, chambre_id, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement st = cx.prepareStatement(req);
                st.setInt(1, cl.getId());  
                System.out.println(cl.getId());
                st.setInt(2, ch.getId());  
          
                st.setDate(3, new java.sql.Date(debut.getTimeInMillis()));  
                
                st.setDate(4, new java.sql.Date(fin.getTimeInMillis()));    
              
                st.setString(5, stat);  

                st.executeUpdate();

                return "reservation done";

            } catch (DateSwitchException e) {
                return "reservation failed";
            }
        }
    }

    public String rechercherReservation(String nom) throws SQLException, DateSwitchException {
        ArrayList<Reservation> resrvs = Hotel.filtrer(
                reservations(),
                res -> res.getClient() != null && nom.equals(res.getClient().getNom())
        );

        if (resrvs.isEmpty()) {
            return "Aucune réservation trouvée pour le client : " + nom + "\n";
        }

        StringBuilder result = new StringBuilder("Réservations pour le client : " + nom + "\n");
        for (Reservation res : resrvs) {
            result.append("\nChambre: ").append(res.getChambre().getType()).append(" d'ID :").append(res.getChambre().getId())
                  .append("\nDate d'arrivée: ").append(res.getDateDebut().getTime())
                  .append("\nDate de départ: ").append(res.getDateFin().getTime())
                  .append("\n\n");
                  
        }
        return result.toString();
    }

    public ArrayList<Chambre> viewRooms(String stat) throws SQLException {
        ArrayList<Chambre> chs = chambres();
       // StringBuilder result = new StringBuilder("Chambres disponibles :\n");
        ArrayList<Chambre> cha=new ArrayList<Chambre>();

        boolean foundAvailable = false;
        for (Chambre ch : chs) {
            if (ch.getStatut().equals(stat)) {
                foundAvailable = true;
                cha.add(ch);
                //result.append("ID: ").append(ch.getId())
                      //.append("\nType: ").append(ch.getType())
                      //.append("\nPrix: ").append(ch.getPrix())
                      //.append("\n\n");
            }
        }

        if (!foundAvailable) {
        	return null;
            //result.append("Aucune chambre disponible actuellement.");
        }

       // return result.toString();
        return cha;
    }
    public String ajouterPaiement(int reservationId, double montant, String methode, String statut, Calendar datePaiement) {
        Connection cx = connexion();
        String insertSql = "INSERT INTO paiement (reservation_id, montant, methode, statut, date_paiement) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = cx.prepareStatement(insertSql)) {
        	
            
            stmt.setInt(1, reservationId);
            stmt.setDouble(2, montant);
            stmt.setString(3, methode);
            stmt.setString(4, statut);
            stmt.setDate(5, new Date(datePaiement.getTimeInMillis()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Paiement ajouté avec succès.";
            } else {
                return "Échec de l'ajout du paiement.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors de l'ajout du paiement : " + e.getMessage();
        }
    }


}
