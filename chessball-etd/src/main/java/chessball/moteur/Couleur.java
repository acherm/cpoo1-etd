package chessball.moteur;

/** Les deux camps. Les Bleus occupent les rangées 1-2 et attaquent la rangée 8. */
public enum Couleur {
    BLEUS, ROUGES;

    public Couleur adverse() { return this == BLEUS ? ROUGES : BLEUS; }

    /** Rangée de fond visée par cette équipe (réponse client n° 9), en coordonnées internes 0..7. */
    public int rangeeAdverse() { return this == BLEUS ? 7 : 0; }
}
