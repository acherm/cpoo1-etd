package chessball.moteur;

/**
 * Comportement interchangeable de l'adversaire automatique (TP4 Q6, nommé
 * au TP5 : le patron Stratégie).
 *
 * <p><b>Contrat</b> : l'implémentation renvoie toujours un coup, jamais
 * {@code null}. Elle n'est pas tenue de renvoyer un coup légal : la partie
 * refuse proprement un coup illégal (elle ne fait pas confiance à la
 * stratégie) et le tour est perdu.</p>
 */
@FunctionalInterface
public interface StrategieAdversaire {
    Coup choisirCoup(Partie partie);
}
