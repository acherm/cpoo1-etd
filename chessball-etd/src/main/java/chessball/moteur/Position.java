package chessball.moteur;

/**
 * Case du plateau, en coordonnées internes 0..7 (colonne = a..h, rangee = 1..8).
 * Classe-valeur immuable : {@code record} (TP3 Q2).
 */
public record Position(int colonne, int rangee) {

    public Position {
        if (colonne < 0 || colonne > 7 || rangee < 0 || rangee > 7) {
            throw new IllegalArgumentException("hors plateau : " + colonne + "," + rangee);
        }
    }

    /** Fabrique lisible : {@code Position.of("b2")}. */
    public static Position of(String algebrique) {
        if (algebrique == null || algebrique.length() != 2) {
            throw new IllegalArgumentException("notation attendue « a1 ».. « h8 » : " + algebrique);
        }
        return new Position(algebrique.charAt(0) - 'a', algebrique.charAt(1) - '1');
    }

    @Override public String toString() { return "" + (char) ('a' + colonne) + (char) ('1' + rangee); }
}
