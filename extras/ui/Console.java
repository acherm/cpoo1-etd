package chessball.ui;

import chessball.moteur.Ballon;
import chessball.moteur.Case;
import chessball.moteur.Couleur;
import chessball.moteur.Partie;
import chessball.moteur.Piece;
import chessball.moteur.Plateau;
import chessball.moteur.Position;

/**
 * Affichage texte d'une partie (TP4 Q7). Le moteur ne sait rien de cette
 * classe : c'est elle qui lit le moteur, jamais l'inverse.
 *
 * <p>Légende : initiale de la pièce (D, T, F, C), en majuscule pour les Bleus
 * et en minuscule pour les Rouges ; {@code *} après la porteuse du ballon ;
 * {@code o} pour le ballon libre ; {@code .} pour une case vide.</p>
 */
public final class Console {
    private Console() {}

    /** Le plateau, rangée 8 en haut, plus le score et le trait. */
    public static String rendu(Partie partie) {
        Plateau plateau = partie.plateau();
        Piece porteuse = partie.ballon().flatMap(Ballon::porteuse).orElse(null);
        StringBuilder sb = new StringBuilder();
        for (int r = Plateau.COTE - 1; r >= 0; r--) {
            sb.append(r + 1).append("  ");
            for (int c = 0; c < Plateau.COTE; c++) {
                Case kase = plateau.caseA(new Position(c, r));
                sb.append(symbole(kase, porteuse)).append(' ');
            }
            sb.append('\n');
        }
        sb.append("   a  b  c  d  e  f  g  h\n");
        sb.append("Score Bleus ").append(partie.score().buts(Couleur.BLEUS))
          .append(" - Rouges ").append(partie.score().buts(Couleur.ROUGES))
          .append("   période ").append(partie.periode())
          .append("   statut ").append(partie.statut());
        if (partie.trait() != null) sb.append("   trait ").append(partie.trait());
        return sb.append('\n').toString();
    }

    private static String symbole(Case kase, Piece porteuse) {
        if (kase.piece().isEmpty()) return kase.porteBallonLibre() ? "o " : ". ";
        Piece p = kase.piece().get();
        char lettre = p.type().name().charAt(0);
        if (p.couleur() == Couleur.ROUGES) lettre = Character.toLowerCase(lettre);
        return lettre + (p == porteuse ? "*" : " ");
    }

    public static void afficher(Partie partie) { System.out.print(rendu(partie)); }
}
