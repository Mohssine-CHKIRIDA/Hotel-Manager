package projet;

public class Client {
 private int id;
 private String nom;
 private String email;
 private String telephone;

 public Client(int id, String nom, String email, String telephone) {
     this.id = id;
     this.nom = nom;
     this.email = email;
     this.telephone = telephone;
 }
 public int getId() {
     return id;
 }
 public void setId(int id) {
	 this.id=id;
 }

 public String getNom() {
     return nom;
 }

 public void setNom(String nom) {
     this.nom = nom;
 }

 public String getEmail() {
     return email;
 }

 public void setEmail(String email) {
     this.email = email;
 }

 public String getTelephone() {
     return telephone;
 }

 public void setTelephone(String telephone) {
     this.telephone = telephone;
 }
}

