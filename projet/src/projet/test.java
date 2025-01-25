package projet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class test {
	public static void main(String[] args) throws DateSwitchException, PaiementNotCompleted, IOException {
      
        Client client = new Client( 3,"Albert Einstein", "albert.einstein@example.com", "0601234567");
        Chambre chambre = new Chambre(8, "Chambre Familiale", 150.00, "disponible");
        GregorianCalendar tmr = new GregorianCalendar();
        tmr.setTimeInMillis(System.currentTimeMillis() + 166400000L);
        Reservation reservation = new Reservation(client,chambre,new GregorianCalendar() ,tmr, "en");
        Paiement paiement = new Paiement(reservation, 150.00, "Carte Bancaire", "effectué", new GregorianCalendar());
        ArrayList<Chambre> chambres =new ArrayList<Chambre>();
        chambres.add(chambre);
        ArrayList<Reservation> res =new ArrayList<Reservation>();
        res.add(reservation);
        ArrayList<Client> clients=new ArrayList<Client>();
        clients.add(client);
        Hotel MC =new Hotel();
        //MC.PaimentTicket(paiement);
        clients.add(new Client( 1,"Marie Curie", "marie.curie@example.com", "0612345678"));
        clients.add(new Client( 2,"Albert Einstein", "albert.einstein@example.com", "0623456789"));
        ArrayList<Client> clientsExample = Hotel.filtrer(clients, Client -> Client.getEmail().contains("example.com"));
        //for (Client cl : clientsExample) {
           // System.out.println(cl.getNom());
        //}
        
        data data=new data();
        String status = "confirmée";
        System.out.println(data.addClientIfNotExists("John", "john@example", "06128"));
       // try {
		//	String result = data.reserver(client, chambre, new GregorianCalendar(), tmr, status);
		//} catch (ChambreIndisponibleException | SQLException e) {
		//	e.printStackTrace();
		//}

}}
