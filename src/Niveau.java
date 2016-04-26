import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe représentant un niveau de jeu.
 * 
 * Attention, cette classe est intentionnellement mal conçue. En particulier, 
 * elle met en oeuvre des données redondantes.  
 */
public class Niveau {
	// nombre de lignes et de colonnes du niveau
	private int nbc, nbl;
	// ce qui doit être affiché
	private char[] tab;
	// coordonnées de l'entrée et de la sortie (redondance avec tab)
	private Point entrée, sortie;
	// liste des coordonnées des mulots (redondance avec tab) 
	private ArrayList<Point> mulots = new ArrayList<Point>();

	private static int convert(char symbole) {
		return symbole - '0' - 1;
	}
	
	private static char convert(int num) {
		return (char) ('0' + num);
	}
	
	private char get(int x, int y) {
		return tab[y * nbc + x];
	}

	private void set(int x, int y, char c) {
		tab[y * nbc + x] = c;
	}
	
	/**
	 * Construit un niveau vide.
	 * @param nbc et nbl Nombre de lignes et de colonnes
	 * @param ex et ey Coordonnée de l'entrée
	 * @param sx et sy Coordonnée de la sortie
	 */
	public Niveau(int nbc, int nbl, int ex, int ey, int sx, int sy) {
		this.nbc = nbc;
		this.nbl = nbl;
		tab = new char[nbc * nbl];
		Arrays.fill(tab, ' ');
		entrée = new Point(ex, ey);
		sortie = new Point(sx, sy);
		set(ex, ey, 'E');
		set(sx, sy, 'S');
	}

	/**
	 * Ajoute une plateforme au niveau.
	 * @param gx et gy Coordonnée du point gauche de la palteforme
	 * @param nb Longueur de la plateforme
	 */
	public void plateforme(int gx, int gy, int nb) {
		for (int col = gx; col < gx + nb; ++col)
			set(col, gy, '-');
	}

	/**
	 * Affiche l'état courant du niveau.
	 */
	public void afficher() {
		for (int lig = 0; lig < nbl; ++lig) {
			for (int col = 0; col < nbc; ++col)
				System.out.print(get(col, lig));
			System.out.println();
		}
	}

	/**
	 * Ajoute un mulot au niveau.
	 */
	public void naissance() {
		// la case en dessous de l'entrée doit être libre
		if (get(entrée.x, entrée.y + 1) != ' ')
			return;
		// un mulot est représenté par une entrée dans la liste
		// la référence est mise à null lorsqu'il sort du niveau
		mulots.add(new Point(entrée.x, entrée.y + 1));
		set(entrée.x, entrée.y + 1, convert(mulots.size()));
	}

	/**
	 * Fait avancer chaque mulot encore présent.
	 */
	public void tic() {
		for (Point mulot : mulots) {
			if (mulot != null)
				if (!chute(mulot))
					avance(mulot);
		}

	}

	private boolean avance(Point coord) {
		if (coord.x + 1 >= nbc || (get(coord.x + 1, coord.y) != ' '
				&& get(coord.x + 1, coord.y) != 'S'))
			return false;
		return déplacer(coord, 1, 0);
	}

	private boolean chute(Point coord) {
		if (coord.y + 1 >= nbl || (get(coord.x, coord.y + 1) != ' '
				&& get(coord.x, coord.y + 1) != 'S'))
			return false;
		return déplacer(coord, 0, 1);
	}

	private boolean déplacer(Point coord, int dx, int dy) {
		Point c = new Point(coord.x + dx, coord.y + dy);
		// sortie des limites de l'ecran ?
		if (c.x >= nbc || c.y >= nbl) 
			return false;
		char symbole = get(coord.x, coord.y);
		set(coord.x, coord.y, ' '); // on efface le mulot
		set(c.x, c.y, symbole); // le revoila juste a cote
		mulots.set(convert(symbole), c);
		// il est sorti ?
		if (c.equals(sortie)) {
			set(c.x, c.y, 'S'); 
			mulots.set(convert(symbole), null);
		}
		return true;
	}

	/**
	 * Indique si le niveau est fini (i.e. tous les mulots sont sortis).
	 */
	public boolean fini() {
		for (Point c : mulots)
			if (c != null)
				return false;
		return true;
	}
}
