package chessball.moteur;

/**
 * Une action d'un tour : une pièce (repérée par sa case d'origine) agit dans
 * une direction. Quatre classes l'implémentent : {@link Deplacement},
 * {@link Poussee}, {@link Tacle}, {@link Saut}. L'interface n'est pas scellée :
 * un {@code switch} sur un coup doit avoir un {@code default}.
 */
public interface Coup {
    Position origine();
    Direction direction();
}
