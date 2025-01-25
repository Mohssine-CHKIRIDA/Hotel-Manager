package projet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.Predicate;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
public class Hotel {
	ArrayList<Chambre> chambres;
	ArrayList<Reservation> reservations;
	ArrayList<Client> clients;
	int nbr_chambre_disponible;
	public Hotel() {}
	
	public static <T> ArrayList<T> filtrer(ArrayList<T> liste, Predicate<T> critere) {
		ArrayList<T> resultat = new ArrayList<T>();
        for (T element : liste) {
            if (critere.test(element)) {
                resultat.add(element);
            }
        }
        return resultat; 
    }
	public  Map<Chambre,Calendar> trierChambres(){
		Map<Chambre,Calendar> chambres=new Hashtable<Chambre,Calendar>();
		ArrayList<Reservation> reservations= filtrer(this.reservations, res -> res.getStatut().equals("confirmée"));
		Collections.sort(reservations);
		for(Reservation res :reservations) {
			Chambre ch=res.getChambre();
			if(chambres.containsKey(ch)) {
				chambres.put(ch,res.getDateFin());
			}
			else {
				chambres.put(ch,res.getDateFin());
			}
		}
		return chambres;
	}
	public String PaimentTicket(Paiement p) throws PaiementNotCompleted, IOException {

		if(p.getStatut()!="effectué") {
			throw new PaiementNotCompleted();
		}
		else {
			Path ticketpath = Paths.get("C:/Users/Hp/eclipse-workspace/projet/ticket.txt");
			Path ticketFile;
			try {
			    ticketFile= Files.createFile(ticketpath);
				}
			catch(IOException e) {}
			Reservation r=p.getReservation();
			String ticket=new String();
			ticket+=("=================================\n");
	        ticket+=("          Hôtel MC           \n");
	        ticket+=("        Ticket de Paiement        \n");
	        ticket+=("=================================\n");
	        ticket+="Date : "+(new SimpleDateFormat("dd/MM/yyyy")).format((p.getDatePaiement()).getTime())+"\n";
	        ticket+=("Client : "+(r.getClient()).getNom()+"\n");
	        ticket+=("Email : "+(r.getClient()).getEmail()+"\n");
	        ticket+=("---------------------------------\n");
	        ticket+=("Détails de la Réservation :\n");
	        ticket+=("Chambre : "+(r.getChambre()).getId()+"\n");
	        ticket+=("Prix/nuit : "+(r.getChambre()).getPrix()+" EUR\n");
	        ticket+=("Du : "+(new SimpleDateFormat("dd/MM/yyyy")).format((r.getDateDebut()).getTime())+"\n");
	        ticket+=("Au : "+(new SimpleDateFormat("dd/MM/yyyy")).format((r.getDateFin()).getTime())+"\n");
	        ticket+=("---------------------------------\n");
	        ticket+=("Montant payé : "+p.getMontant()+" EUR\n");
	        ticket+=("Méthode : "+p.getMethode()+"\n");
	        ticket+=("Statut : "+p.getStatut()+"\n");
	        ticket+=("=================================\n");
	        ticket+=("Merci pour votre visite !\n");
	        Files.writeString(ticketpath, ticket,StandardOpenOption.CREATE);
	        return ticket;
	        //System.out.printf(Files.readString(ticketpath)); //pour afficher le ticket
		}
		
	}
	
}
