package chessball.moteur;

/**
 * Sauter par-dessus la case voisine occupée (pièce ou ballon) pour atterrir
 * deux cases plus loin, sur une case libre (attaquants seulement).
 */
public final class Saut extends CoupDirige {
    public Saut(Position origine, Direction direction) { super(origine, direction); }
    @Override boolean saute() { return true; }
}
