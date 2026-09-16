package chessball.moteur;

/**
 * Tacler une pièce adverse voisine : le défenseur prend sa place, la victime
 * est repoussée d'une case de plus (défenseurs seulement).
 */
public final class Tacle extends CoupDirige {
    public Tacle(Position origine, Direction direction) { super(origine, direction); }
}
