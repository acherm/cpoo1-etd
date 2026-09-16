package chessball.moteur;

/** Un pas d'une case dans une direction, vers une case libre (toute pièce). */
public final class Deplacement extends CoupDirige {
    public Deplacement(Position origine, Direction direction) { super(origine, direction); }
}
