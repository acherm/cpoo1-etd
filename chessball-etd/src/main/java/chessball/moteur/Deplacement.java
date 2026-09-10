package chessball.moteur;

public record Deplacement(Position de, Position vers) implements Coup {
    @Override public Position origine() { return de; }
}
