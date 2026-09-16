package chessball.moteur;

/**
 * Les deux camps. Les Bleus partent du haut (rangs 5 et 6) et marquent sur le
 * rang 1 ; les Rouges partent du bas et marquent sur le rang 6.
 */
public enum Couleur {
    BLEUS, ROUGES;

    public Couleur adverse() { return this == BLEUS ? ROUGES : BLEUS; }

    /** Rangée (0..5) où CETTE équipe marque : la ligne de but adverse. */
    public int rangeeDeBut() { return this == BLEUS ? 0 : Position.RANGEES - 1; }

    /** Rangée (0..5) de sa propre ligne de but, celle qu'elle défend. */
    public int rangeeDeDepart() { return this == BLEUS ? Position.RANGEES - 1 : 0; }
}
