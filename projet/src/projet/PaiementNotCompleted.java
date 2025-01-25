package projet;

public class PaiementNotCompleted extends Exception {
	public PaiementNotCompleted() {
		super("paiement n'est pas effectuee");
	}

}
