package chessball.moteur;

/**
 * Pousser le ballon : la pièce prend la place du ballon (case voisine), le
 * ballon avance d'une case de plus (toute pièce).
 */
public final class Poussee extends CoupDirige {
    public Poussee(Position origine, Direction direction) { super(origine, direction); }
}
