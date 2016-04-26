import java.util.Scanner;

import static outils.Outils.*;

/**
 * Application illustrant la lecture non bloquante de données au clavier.
 * De plus, ce programme illustre ce à quoi pourrait ressembler l'interface textuelle
 * du projet.
 */
public class AppliTexte {
	/** Plus VITESSE est grand, plus les mulots se déplacent rapidement */
	private static final double VITESSE = 5.;
	
	public static void main(String[] args)  {
		// un niveau de 40 colonnes et 20 lignes avec l'entrée en 3,1 et la sortie en 38,17
		Niveau niv1 = new Niveau(40, 20, 3, 1, 38, 17);
		// une plateforme en 2,18 de largeur 37
		niv1.plateforme(2, 18, 37); 
		niv1.afficher();

		// naissance de 2 mulots
		niv1.naissance(); 
		niv1.afficher();
		pause(1/VITESSE);
		niv1.tic(); 
		niv1.naissance();
		niv1.afficher();
		pause(1/VITESSE);

		// boucle de jeu
		boolean escape = false;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		do {
			// clavier() retourne vrai si des données sont prêtes à être lues
			if (clavier()) {
				String s = sc.next();
				// A priori le test suivant est inutile car sc.next() retourne toujours
				// un mot non-vide.
				if (s.length() >= 1) {
					switch (s.charAt(0)) {
					case 'q':
					case 'Q':
						escape = true;
						break;
					default:
						System.out.println("not yet implemented");
						// une petite pause pour que le message puisse être lu à l'écran
						pause(2);
					}
				}
			} else
				niv1.tic();
			System.out.println("tapez q (suivi de 'return') pour abandonner");
			niv1.afficher();
			pause(1/VITESSE);
		} while (!escape && !niv1.fini());
	}
}
