package chessball.moteur;

public record Tir(Position porteuse, Direction direction) implements Coup {
    @Override public Position origine() { return porteuse; }
}
