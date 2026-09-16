package chessball.moteur;

import java.util.Objects;

/**
 * Ce que {@code Partie.jouer} renvoie : le coup est accepté ou refusé (avec
 * son motif), et s'il a marqué un but. Classe-valeur immuable, écrite à la
 * main comme {@link Position} ; on la construit par ses trois fabriques.
 */
public final class ResultatCoup {
    private final boolean accepte;
    private final Refus refus;
    private final boolean but;

    private ResultatCoup(boolean accepte, Refus refus, boolean but) {
        this.accepte = accepte;
        this.refus = Objects.requireNonNull(refus);
        this.but = but;
    }

    /** Coup accepté, sans but. */
    public static ResultatCoup ok() { return new ResultatCoup(true, Refus.AUCUN, false); }

    /** Coup accepté qui marque un but. */
    public static ResultatCoup okAvecBut() { return new ResultatCoup(true, Refus.AUCUN, true); }

    /** Coup refusé pour le motif donné (jamais {@link Refus#AUCUN}). */
    public static ResultatCoup refuse(Refus refus) {
        if (refus == Refus.AUCUN) throw new IllegalArgumentException("un refus a un motif");
        return new ResultatCoup(false, refus, false);
    }

    public boolean accepte() { return accepte; }
    public Refus refus()     { return refus; }
    public boolean but()     { return but; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultatCoup r)) return false;
        return accepte == r.accepte && refus == r.refus && but == r.but;
    }

    @Override public int hashCode() { return Objects.hash(accepte, refus, but); }

    @Override public String toString() {
        return accepte ? (but ? "accepté (but)" : "accepté") : "refusé : " + refus;
    }
}
