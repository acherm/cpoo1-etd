package chessball.moteur;

public record Passe(Position porteuse, Position cible) implements Coup {
    @Override public Position origine() { return porteuse; }
}
