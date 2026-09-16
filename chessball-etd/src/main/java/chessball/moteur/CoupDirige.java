package chessball.moteur;

import java.util.Objects;

/**
 * Socle des quatre coups : une origine, une direction, l'égalité structurelle
 * et la notation « origine-arrivée » (LAN sans suffixe). Visibilité paquet : ce
 * qui compte pour le reste du monde, c'est {@link Coup}.
 */
abstract class CoupDirige implements Coup {
    private final Position origine;
    private final Direction direction;

    CoupDirige(Position origine, Direction direction) {
        this.origine = Objects.requireNonNull(origine, "origine");
        this.direction = Objects.requireNonNull(direction, "direction");
    }

    @Override public Position origine()   { return origine; }
    @Override public Direction direction() { return direction; }

    /** La case où la pièce agissante arrive : la voisine, ou deux cases plus loin pour un saut. */
    Position arrivee() {
        Position q1 = origine.voisine(direction).orElse(null);
        if (q1 == null) return null;
        return saute() ? q1.voisine(direction).orElse(null) : q1;
    }

    boolean saute() { return false; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;
        CoupDirige c = (CoupDirige) o;
        return origine.equals(c.origine) && direction == c.direction;
    }

    @Override public int hashCode() { return Objects.hash(getClass(), origine, direction); }

    @Override public String toString() {
        Position a = arrivee();
        return origine + "-" + (a == null ? "hors" : a.toString());
    }
}
