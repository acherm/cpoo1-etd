package chessball.moteur;

/** Les huit directions du roi : toute action se fait d'une case à sa voisine. */
public enum Direction {
    NORD(0, 1), SUD(0, -1), EST(1, 0), OUEST(-1, 0),
    NORD_EST(1, 1), NORD_OUEST(-1, 1), SUD_EST(1, -1), SUD_OUEST(-1, -1);

    private final int dc, dr;
    Direction(int dc, int dr) { this.dc = dc; this.dr = dr; }
    public int dColonne() { return dc; }
    public int dRangee() { return dr; }
}
