package chessball.moteur;

/** Déplacer une pièce de « de » à « vers ». Classe-valeur : deux déplacements identiques sont égaux. */
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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deplacement autre)) return false;
        return java.util.Objects.equals(de, autre.de) && java.util.Objects.equals(vers, autre.vers);
    }

    @Override public int hashCode() { return java.util.Objects.hash(de, vers); }

    @Override public String toString() { return "Deplacement(" + de + " -> " + vers + ")"; }
}
