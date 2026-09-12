package chessball.moteur;

import java.util.Objects;

/**
 * Case du plateau, en coordonnées internes 0..7 (colonne = a..h, rangee = 1..8).
 * Classe-valeur immuable, écrite à la main (voir le bonus : en Java 17+, on
 * écrirait cette classe en une ligne avec un {@code record} — cf. la fiche
 * « Du modèle au code »).
 */
public final class Position {
    private final int colonne;
    private final int rangee;

    public Position(int colonne, int rangee) {
        if (colonne < 0 || colonne > 7 || rangee < 0 || rangee > 7) {
            throw new IllegalArgumentException("hors plateau : " + colonne + "," + rangee);
        }
        this.colonne = colonne;
        this.rangee = rangee;
    }

    /** Fabrique lisible : {@code Position.of("b2")}. */
    public static Position of(String algebrique) {
        if (algebrique == null || algebrique.length() != 2) {
            throw new IllegalArgumentException("notation attendue « a1 ».. « h8 » : " + algebrique);
        }
        return new Position(algebrique.charAt(0) - 'a', algebrique.charAt(1) - '1');
    }

    public int colonne() { return colonne; }
    public int rangee()  { return rangee; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position p)) return false;
        return colonne == p.colonne && rangee == p.rangee;
    }

    @Override public int hashCode() { return Objects.hash(colonne, rangee); }

    @Override public String toString() { return "" + (char) ('a' + colonne) + (char) ('1' + rangee); }
}
