package crazyEight;
//import source pour afficher l'heure
import java.util.Date; 

public class ControleJeu {
	private static Date tempsDeJeu;
	//la sens l'horaire par default (false: sens horaire, true: sens antihoraire)
	private boolean sensPartie=false;
	private JoueurVirtuel joueurEnCours;
	
	public boolean isSensPartie() {
		return sensPartie;
	}

	public void setSensPartie(boolean sensPartie) {
		this.sensPartie = sensPartie;
	}
	
	public JoueurVirtuel afficherJEnCours() {
		joueurEnCours=joueur
		return joueurEnCours;
	}

	/*public static void main(String[] args) {
		Date date = new Date();
		tempsDeJeu=date;
		System.out.print(tempsDeJeu);
	}*/
}
