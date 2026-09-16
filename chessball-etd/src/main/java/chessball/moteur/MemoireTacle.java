package chessball.moteur;

import java.util.Objects;

/**
 * Le souvenir du dernier coup, s'il était un tacle : la case où se tient
 * maintenant le tacleur, et celle où sa victime a été repoussée. Elle interdit
 * les représailles immédiates de la victime contre le tacleur.
 */
public final class MemoireTacle {
    private final Position tacleur;
    private final Position victime;

    public MemoireTacle(Position tacleur, Position victime) {
        this.tacleur = Objects.requireNonNull(tacleur);
        this.victime = Objects.requireNonNull(victime);
    }

    public Position tacleur() { return tacleur; }
    public Position victime() { return victime; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemoireTacle m)) return false;
        return tacleur.equals(m.tacleur) && victime.equals(m.victime);
    }

    @Override public int hashCode() { return Objects.hash(tacleur, victime); }

    @Override public String toString() { return tacleur.toString() + victime; }
}
