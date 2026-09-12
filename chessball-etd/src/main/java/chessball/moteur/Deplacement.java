package chessball.moteur;

public final class Deplacement implements Coup {
    private final Position de;
    private final Position vers;

    public Deplacement(Position de, Position vers) {
        this.de = de;
        this.vers = vers;
    }

    public Position de()   { return de; }
    public Position vers() { return vers; }

    @Override public Position origine() { return de; }
}
