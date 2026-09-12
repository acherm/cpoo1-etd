package chessball.moteur;

/**
 * Les trois actions possibles d'un tour (réponse client n° 14 : une seule par tour).
 * Interface ordinaire : contrairement à un {@code sealed interface}, rien ne
 * garantit ici qu'on a bien traité les trois implémentations — voir le
 * {@code default} du {@code switch} dans {@code Partie.jouer} (et le bonus,
 * qui vous fait retrouver cette garantie avec {@code sealed} + {@code record}).
 */
public interface Coup {
    /** Case de la pièce qui agit — commune aux trois coups. */
    Position origine();
}
