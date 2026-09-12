package chessball.moteur;

public final class Passe implements Coup {
    private final Position porteuse;
    private final Position cible;

    public Passe(Position porteuse, Position cible) {
        this.porteuse = porteuse;
        this.cible = cible;
    }

    public Position porteuse() { return porteuse; }
    public Position cible()    { return cible; }

    @Override public Position origine() { return porteuse; }
}
