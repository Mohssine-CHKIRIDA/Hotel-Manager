package projet;

import java.util.Calendar;

public class Paiement {
    private Reservation reservation;
    private double montant;
    private String methode;
    private String statut;
    private Calendar datePaiement;

    public Paiement( Reservation reservation, double montant, String methode, String statut, Calendar datePaiement) {
       
        this.reservation = reservation;
        this.montant = montant;
        this.methode = methode;
        this.statut = statut;
        this.datePaiement = datePaiement;
    }


    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getStatut() {
        return statut;
    }
//échoué ,en attente , effectué
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Calendar getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Calendar datePaiement) {
        this.datePaiement = datePaiement;
    }
}