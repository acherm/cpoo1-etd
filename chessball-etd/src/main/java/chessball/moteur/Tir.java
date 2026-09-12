package chessball.moteur;

public final class Tir implements Coup {
    private final Position porteuse;
    private final Direction direction;

    public Tir(Position porteuse, Direction direction) {
        this.porteuse = porteuse;
        this.direction = direction;
    }

    public Position porteuse()   { return porteuse; }
    public Direction direction() { return direction; }

    @Override public Position origine() { return porteuse; }
}
