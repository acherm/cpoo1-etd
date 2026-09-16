package chessball.moteur;

import java.util.Objects;
import java.util.Optional;

/**
 * Case du plateau 7×6, en coordonnées internes : colonne 0..6 (a..g), rangée
 * 0..5 (rang 1 en bas .. rang 6 en haut). Classe-valeur immuable, écrite à la
 * main (en Java 17+, un {@code record} tiendrait en une ligne : voir la fiche
 * « Du modèle au code »).
 */
public final class Position {
    public static final int COLONNES = 7;
    public static final int RANGEES = 6;

    private final int colonne;
    private final int rangee;

    public Position(int colonne, int rangee) {
        if (colonne < 0 || colonne >= COLONNES || rangee < 0 || rangee >= RANGEES) {
            throw new IllegalArgumentException("hors plateau : " + colonne + "," + rangee);
        }
        this.colonne = colonne;
        this.rangee = rangee;
    }

    /** Fabrique lisible : {@code Position.of("d4")}. */
    public static Position of(String algebrique) {
        if (algebrique == null || algebrique.length() != 2) {
            throw new IllegalArgumentException("notation attendue « a1 ».. « g6 » : " + algebrique);
        }
        return new Position(algebrique.charAt(0) - 'a', algebrique.charAt(1) - '1');
    }

    public int colonne() { return colonne; }
    public int rangee()  { return rangee; }

    /** La case voisine dans cette direction, ou vide si elle sort du plateau. */
    public Optional<Position> voisine(Direction d) {
        int c = colonne + d.dColonne(), r = rangee + d.dRangee();
        if (c < 0 || c >= COLONNES || r < 0 || r >= RANGEES) return Optional.empty();
        return Optional.of(new Position(c, r));
    }

    /** Zones de touche : colonnes a et g, rangs 2 à 5. Interdites au ballon. */
    public boolean estZoneDeTouche() {
        return (colonne == 0 || colonne == COLONNES - 1) && rangee >= 1 && rangee <= RANGEES - 2;
    }

    /** Rangs 1 et 6, coins compris. */
    public boolean estLigneDeBut() { return rangee == 0 || rangee == RANGEES - 1; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position p)) return false;
        return colonne == p.colonne && rangee == p.rangee;
    }

    @Override public int hashCode() { return Objects.hash(colonne, rangee); }

    @Override public String toString() { return "" + (char) ('a' + colonne) + (char) ('1' + rangee); }
}
