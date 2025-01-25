package projet;

public class Chambre  {
    private int id;
    private String type;
    private double prix;
    private String statut;
    
    public Chambre(int id, String type, double prix, String statut) {
        this.id = id;
        this.type = type;
        this.prix = prix;
        this.statut = statut;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrix() {
        return this.prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getStatut() {
        return this.statut;
    }
// en nettoyage ,occupée ,disponible 
    public void setStatut(String statut) {
        this.statut = statut;
    }
}
