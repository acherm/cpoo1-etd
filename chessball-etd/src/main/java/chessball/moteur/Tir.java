package chessball.moteur;

/** La porteuse propulse le ballon dans une direction. Classe-valeur. */
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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tir autre)) return false;
        return java.util.Objects.equals(porteuse, autre.porteuse) && java.util.Objects.equals(direction, autre.direction);
    }

    @Override public int hashCode() { return java.util.Objects.hash(porteuse, direction); }

    @Override public String toString() { return "Tir(" + porteuse + " -> " + direction + ")"; }
}
