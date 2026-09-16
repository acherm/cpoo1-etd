package chessball.moteur;

/**
 * Motifs de refus d'un coup. Un {@code enum}, jamais une chaîne libre : un
 * test qui compare des {@code String} finit toujours par une faute de frappe
 * silencieuse. {@link #AUCUN} signifie « coup légal ».
 */
public enum Refus {
    AUCUN,
    PAS_DE_PIECE,
    PAS_SON_TOUR,
    PAS_PORTEUSE,
    CIBLE_NON_COEQUIPIERE,
    NON_ALIGNEE,
    TRAJECTOIRE_OBSTRUEE,
    CASE_OCCUPEE,
    PARTIE_TERMINEE,
    DIRECTION_IMPOSSIBLE
}
