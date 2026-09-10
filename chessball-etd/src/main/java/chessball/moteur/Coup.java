package chessball.moteur;

/**
 * Les trois actions possibles d'un tour (réponse client n° 14 : une seule par tour).
 * Interface {@code sealed} : le {@code switch} du moteur est vérifié exhaustif
 * par le compilateur (TP3 Q3).
 */
public sealed interface Coup permits Deplacement, Passe, Tir {
    /** Case de la pièce qui agit — commune aux trois coups. */
    Position origine();
}
