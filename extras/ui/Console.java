package chessball.ui;

import chessball.moteur.Case;
import chessball.moteur.Couleur;
import chessball.moteur.Partie;
import chessball.moteur.Piece;
import chessball.moteur.Plateau;
import chessball.moteur.Position;

/**
 * Affichage texte d'une partie (TP4). Le moteur ne sait rien de cette classe :
 * c'est elle qui lit le moteur, jamais l'inverse.
 *
 * <p>Légende : {@code A}/{@code D} attaquant et défenseur bleus, {@code a}/{@code d}
 * rouges, {@code o} le ballon, {@code .} une case vide. Rangée 6 en haut.</p>
 */
public final class Console {
    private Console() {}

    public static String rendu(Partie partie) {
        Plateau plateau = partie.plateau();
        StringBuilder sb = new StringBuilder();
        for (int r = Position.RANGEES - 1; r >= 0; r--) {
            sb.append(r + 1).append("  ");
            for (int c = 0; c < Position.COLONNES; c++) {
                sb.append(symbole(plateau.caseA(new Position(c, r)))).append(' ');
            }
            sb.append('\n');
        }
        sb.append("   a b c d e f g\n");
        sb.append("statut ").append(partie.statut()).append("   trait ").append(partie.trait());
        partie.memoireTacle().ifPresent(m -> sb.append("   mémoire de tacle ").append(m));
        return sb.append('\n').toString();
    }

    private static char symbole(Case kase) {
        if (kase.porteBallon()) return 'o';
        if (kase.piece().isEmpty()) return '.';
        Piece p = kase.piece().get();
        char lettre = p.type().name().charAt(0);
        return p.couleur() == Couleur.ROUGES ? Character.toLowerCase(lettre) : lettre;
    }

    public static void afficher(Partie partie) { System.out.print(rendu(partie)); }
}
