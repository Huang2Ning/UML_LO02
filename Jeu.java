package crazyeightBis;

import java.util.ArrayList;
import java.util.Scanner;


public class Jeu {

	private Initialisation initialisation;
	private Pioche pioche;
	private Talon talon;
	private int nbJoueurs;
	protected ArrayList<Joueur> listeJoueurs = new ArrayList<Joueur>();
	private boolean gagnant=false; //à envoyer vers le main (partie)
	private Joueur joueurEnCours;   //pour savoir qui faire jouer à chaque étape d'un tour
	private ControleJeu controleJeu;
	private int posJoueurEnCours;
	
	Scanner sc = new Scanner(System.in);
	
	//doit avoir créé une initialisation d'abord !
	//constructeur du jeu avec les paramètres de initialisation
	public Jeu(Pioche pioche, Talon talon, int nbJoueurs, ArrayList<Joueur> listeJoueurs) {
		this.pioche = pioche;
		this.talon = talon;
		this.nbJoueurs = nbJoueurs;
		this.listeJoueurs = listeJoueurs;
		this.controleJeu = new ControleJeu();
		posJoueurEnCours=this.listeJoueurs.size()-1; //si on veut commencer en premier à jouer
		System.out.println("\nla premiere carte du talon est : " +this.talon.getCarteDessus());
	}


//méthode de tour de jeu  (à appeler une fois que tous les éléments ont étés initialisés avec l'objet initialisation)
	public boolean tourDeJeu(Joueur joueurEnCours){
		
		
		this.joueurEnCours = this.listeJoueurs.get(posJoueurEnCours); //dans le main il faudra envoyer controleJeu.joueurEnCours dans cette méthode
		ArrayList<Carte> carteJouable= new ArrayList<Carte>();
		carteJouable = this.joueurEnCours.cartesJouables(this.talon.getCarteDessus());
		int nbCartesJouables = carteJouable.size();
		ArrayList<Carte> mainSuppr = new ArrayList<Carte>();
		
		
		if(nbCartesJouables==0){
			this.joueurEnCours.piocherCarte(1, this.pioche);
			System.out.println("\n"+this.joueurEnCours.getNom() +" pioche 1 carte");
		}
		
		
		else if (this.joueurEnCours.getNom()=="moi")  //si c'est à nous de jouer 
		{	
			Carte carteJouee;
			carteJouee=this.joueurEnCours.jouerCarte(carteJouable, this.talon.getCarteDessus());
			this.talon.setCarteDessus(carteJouee); //on joue la carte
			
			mainSuppr = this.joueurEnCours.getMain();
			mainSuppr.remove(carteJouee);
			this.joueurEnCours.setMain(mainSuppr); //on supprime la carte jouée de la main du joueur
			
			//on vérifie si le joueur a encore des cartes (si oui, on continue, si non, il a gagné !)
			if (this.joueurEnCours.getMain().isEmpty()){
				System.out.println("vous n'avez plus de cartes, bravo !");
				this.setGagnant(true);
				return true;
			}
			else{
				int nbCartesReste;
				System.out.println("Il vous reste "  + this.joueurEnCours.getMain().size() +" cartes.\n");	
			}
		}
		
		
		else{		//si le joueur est un robot
		//if stratégie = 0
			//méthode simple : toujours jouer la 1e carte jouable (aucune stratégie)
			Carte carteJouee;
			carteJouee=carteJouable.get(0);
			this.talon.setCarteDessus(carteJouee); //on joue la carte
			
			System.out.println("\n"+ this.joueurEnCours.getNom() +" joue le " +carteJouee);
			
			mainSuppr = this.joueurEnCours.getMain();
			mainSuppr.remove(carteJouee);
			this.joueurEnCours.setMain(mainSuppr);
			
			
			if (this.joueurEnCours.getMain().isEmpty()){
				System.out.println(this.joueurEnCours.getNom() +" n'a plus de cartes");
				this.setGagnant(true);
				return true;
			}
			else{
				int nbCartesReste;
				System.out.println("Il reste "  +" " + this.joueurEnCours.getMain().size() +" cartes à "+ this.joueurEnCours.getNom());	
			}	
		}
		//à partir d'ici le joueur a posé sa carte.
		
		
		
		if (this.talon.getCarteDessus().getEffet()==5){  //s'il y a changement de couleur (à cause d'un 8 par exemple)
			if (this.joueurEnCours.getNom()=="moi"){ //si c'est moi qui joue, le programme me demande ce que je veux comme couleur
				System.out.println("Vous avez joué un "+(this.talon.getCarteDessus().getNumero()+1) + ". -Changement de couleur-  Quelle couleur voulez-vous ?\n 0 pour Coeur\n 1 pour Pique\n 2 pour Carreau\n 3 pour Trèfle\n ");
				int nouvelleCouleur;
				nouvelleCouleur=sc.nextInt();
				//on a la nouvelle couleur, on change la carte du dessus du talon
				Carte nouvelleCarte;
				nouvelleCarte=this.talon.getCarteDessus();
				nouvelleCarte.setCouleur(nouvelleCouleur);
				this.talon.setCarteDessus(nouvelleCarte);
				
				System.out.println("Nouvelle couleur du talon : " +this.talon.getCarteDessus().getCouleur());
				
			}
			else{		//si c'est au robot de choisir, appel de méthode   A CHANGER DONC
				
				//pour le moment le robot ne change pas la couleur
				System.out.println("\nnouvelle carte sur talon : " +this.talon.getCarteDessus() + " (le robot n'a pas changé la couleur)");
			}
		}
			
		else{
			System.out.println("\nnouvelle carte sur talon : " +this.talon.getCarteDessus());
		}
		
		
		
		//changer de sens
		if (this.talon.getCarteDessus().getEffet()==2){
			if (this.controleJeu.isSensPartie()) {
				this.controleJeu.setSensPartie(false);	//si le sens est normal, on le met en "inverse"
			}
			else{
				this.controleJeu.setSensPartie(true);   //si le sens est inverse, on le met en "normal"
			}
		}
		
		//effet rejouer  
	//(et méthode prochainJoueur() pour le déroulement sans effet)
		if (!(this.talon.getCarteDessus().getEffet()==1)) {		//si l'effet rejouer a été activé, on saute cette étape=>le joueur rejoue
			System.out.println(this.joueurEnCours.getNom() +" rejoue !");
			this.prochainJoueur();		//et si effet pas "rejouer" : on change de joueur
		}								
		
		
		//effet sauter prochain joueur  
		if (this.talon.getCarteDessus().getEffet()==3){   //si effet saute tour, prochainJoueur() une 2e fois
			this.prochainJoueur();
		}
		
		
		
		
		
		this.verifPiocheVide();  //fin du tour : on vérifie que la pioche est pas vide/trop petite pour le prochain tour
		
		return gagnant;
	}


	
	
	
	

	public void verifPiocheVide(){   //à faire tourner à chaque fin de tour
		if (this.pioche.getDeck().size()<4){		//si la pioche devient trop petite, on la remplie
			ArrayList<Carte> nouvellePioche = new ArrayList<Carte>();
			
			nouvellePioche =this.pioche.getDeck();
			nouvellePioche.addAll(this.talon.getDessousPile());
			this.pioche.setDeck(nouvellePioche);			//on remplie la pioche avec les cartes du talon
			
			ArrayList<Carte> nouveauTalon = new ArrayList<Carte>();
			nouveauTalon.clear();				
			this.talon.setDessousPile(nouveauTalon);	//on vide le talon
		}
	}

	
	
	public void prochainJoueur(){
		int posJoueurEnCours;
		posJoueurEnCours=this.listeJoueurs.indexOf(joueurEnCours);  //inutile ?
		
		if(controleJeu.isSensPartie()) { //si la partie va dans le sens "normal"

			//this.joueurEnCours = (this.posJoueurEnCours + 1) % getNombreJoueur();
			
			
			posJoueurEnCours=this.listeJoueurs.indexOf(joueurEnCours); //on récupère l'index du joueur précédent
			this.posJoueurEnCours = (posJoueurEnCours +1)%this.listeJoueurs.size(); //on l'incrémente dans la limite du nombre de joueurs 
			this.joueurEnCours=this.listeJoueurs.get(posJoueurEnCours); //on désigne le nouveau joueurEnCours

		}
		//si on va dans le sens inverse de la "normale"
		else{

			if(posJoueurEnCours==0){
				this.posJoueurEnCours = (this.listeJoueurs.size()-1);
				this.joueurEnCours=this.listeJoueurs.get(this.posJoueurEnCours);
				
			}
			else{
				this.posJoueurEnCours--;
				this.joueurEnCours=this.listeJoueurs.get(this.posJoueurEnCours);
			}
		}
	}
	
	
	
	
	//getters and setters
	
	public Initialisation getInitialisation() {
		return initialisation;
	}
	public void setInitialisation(Initialisation initialisation) {
		this.initialisation = initialisation;
	}

	public Pioche getPioche() {
		return pioche;
	}

	public void setPioche(Pioche pioche) {
		this.pioche = pioche;
	}

	public Talon getTalon() {
		return talon;
	}

	public void setTalon(Talon talon) {
		this.talon = talon;
	}

	public int getNbJoueurs() {
		return nbJoueurs;
	}
	public void setNbJoueurs(int nbJoueurs) {
		this.nbJoueurs = nbJoueurs;
	}
	public ArrayList<Joueur> getListeJoueurs() {
		return listeJoueurs;
	}
	public void setListeJoueurs(ArrayList<Joueur> listeJoueurs) {
		this.listeJoueurs = listeJoueurs;
	}


	public boolean isGagnant() {
		return gagnant;
	}


	public void setGagnant(boolean gagnant) {
		this.gagnant = gagnant;
	}


	public Joueur getJoueurEnCours() {
		return joueurEnCours;
	}


	public void setJoueurEnCours(Joueur joueurEnCours) {
		this.joueurEnCours = joueurEnCours;
	}


	public ControleJeu getControleJeu() {
		return controleJeu;
	}


	public void setControleJeu(ControleJeu controleJeu) {
		this.controleJeu = controleJeu;
	}


	public int getPosJoueurEnCours() {
		return posJoueurEnCours;
	}


	public void setPosJoueurEnCours(int posJoueurEnCours) {
		this.posJoueurEnCours = posJoueurEnCours;
	}
	
	
	

	
	
	
}
