package chessball.moteur;

/** Motifs de refus d'un coup : un {@code enum}, jamais une chaîne libre. */
public enum Refus {
    AUCUN,
    PARTIE_TERMINEE,
    PAS_DE_PIECE,
    PAS_SON_TOUR,
    HORS_PLATEAU,
    CASE_NON_LIBRE,
    PAS_DE_BALLON,
    ZONE_DE_TOUCHE,
    PAS_D_ADVERSAIRE,
    RESERVE_AUX_DEFENSEURS,
    RESERVE_AUX_ATTAQUANTS,
    RIEN_A_SAUTER,
    REPRESAILLES
}
