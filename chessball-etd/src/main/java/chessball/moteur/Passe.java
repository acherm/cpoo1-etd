package chessball.moteur;

/** La porteuse envoie le ballon à une coéquipière. Classe-valeur. */
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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passe autre)) return false;
        return java.util.Objects.equals(porteuse, autre.porteuse) && java.util.Objects.equals(cible, autre.cible);
    }

    @Override public int hashCode() { return java.util.Objects.hash(porteuse, cible); }

    @Override public String toString() { return "Passe(" + porteuse + " -> " + cible + ")"; }
}
