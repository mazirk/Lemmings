import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe repr�sentant un niveau de jeu.
 * 
 * Attention, cette classe est intentionnellement mal con�ue. En particulier, 
 * elle met en oeuvre des donn�es redondantes.  
 */
public class Niveau {
	// nombre de lignes et de colonnes du niveau
	private int nbc, nbl;
	// ce qui doit �tre affich�
	private char[] tab;
	// coordonn�es de l'entr�e et de la sortie (redondance avec tab)
	private Point entr�e, sortie;
	// liste des coordonn�es des mulots (redondance avec tab) 
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
	 * @param ex et ey Coordonn�e de l'entr�e
	 * @param sx et sy Coordonn�e de la sortie
	 */
	public Niveau(int nbc, int nbl, int ex, int ey, int sx, int sy) {
		this.nbc = nbc;
		this.nbl = nbl;
		tab = new char[nbc * nbl];
		Arrays.fill(tab, ' ');
		entr�e = new Point(ex, ey);
		sortie = new Point(sx, sy);
		set(ex, ey, 'E');
		set(sx, sy, 'S');
	}

	/**
	 * Ajoute une plateforme au niveau.
	 * @param gx et gy Coordonn�e du point gauche de la palteforme
	 * @param nb Longueur de la plateforme
	 */
	public void plateforme(int gx, int gy, int nb) {
		for (int col = gx; col < gx + nb; ++col)
			set(col, gy, '-');
	}

	/**
	 * Affiche l'�tat courant du niveau.
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
		// la case en dessous de l'entr�e doit �tre libre
		if (get(entr�e.x, entr�e.y + 1) != ' ')
			return;
		// un mulot est repr�sent� par une entr�e dans la liste
		// la r�f�rence est mise � null lorsqu'il sort du niveau
		mulots.add(new Point(entr�e.x, entr�e.y + 1));
		set(entr�e.x, entr�e.y + 1, convert(mulots.size()));
	}

	/**
	 * Fait avancer chaque mulot encore pr�sent.
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
		return d�placer(coord, 1, 0);
	}

	private boolean chute(Point coord) {
		if (coord.y + 1 >= nbl || (get(coord.x, coord.y + 1) != ' '
				&& get(coord.x, coord.y + 1) != 'S'))
			return false;
		return d�placer(coord, 0, 1);
	}

	private boolean d�placer(Point coord, int dx, int dy) {
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
