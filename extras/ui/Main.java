package chessball.ui;

import chessball.moteur.Couleur;
import chessball.moteur.Equipe;
import chessball.moteur.Partie;
import chessball.moteur.Piece;
import chessball.moteur.Plateau;
import chessball.moteur.Position;
import chessball.moteur.ResultatCoup;
import chessball.moteur.Statut;
import chessball.moteur.StrategieAdversaire;
import chessball.moteur.StrategieAleatoire;
import chessball.moteur.TypePiece;

import java.util.List;
import java.util.Random;

/**
 * TP4 Q7 : deux IA aléatoires jouent un match complet (K = 20, la valeur du client) dans la console.
 * Lancement : {@code mvn -q compile exec:java}.
 *
 * <p>La boucle suit la machine à états du match (TD2) : à chaque tour on
 * regarde le statut et on franchit la transition qui convient.</p>
 */
public final class Main {
    /** Tours par équipe et par mi-temps (réponse officielle n° 11). Avec K = 5 une IA aléatoire ne marque presque jamais. */
    static final int K = 20;

    public static void main(String[] args) {
        Plateau plateau = new Plateau();
        Equipe bleus = new Equipe(Couleur.BLEUS);
        Equipe rouges = new Equipe(Couleur.ROUGES);
        installer(plateau, bleus, List.of("a1", "b1", "c1", "d1", "e2", "f2"));
        installer(plateau, rouges, List.of("h8", "g8", "f8", "e8", "d7", "c7"));

        Partie partie = new Partie(plateau, bleus, rouges, K);
        StrategieAdversaire iaBleus = new StrategieAleatoire(new Random());
        StrategieAdversaire iaRouges = new StrategieAleatoire(new Random());

        partie.engager(Couleur.BLEUS, Position.of("d4"));
        System.out.println("Coup d'envoi : les Bleus engagent en d4");
        Console.afficher(partie);

        int tours = 0, refuses = 0;
        while (partie.statut() != Statut.TERMINE) {
            switch (partie.statut()) {
                case ENGAGEMENT -> {                       // après un but : l'encaissant a le trait
                    Position centre = caseCentraleLibre(plateau);
                    if (centre == null) { System.out.println("Aucune case centrale libre : on arrête."); return; }
                    partie.engager(partie.trait(), centre);
                    System.out.println("Engagement des " + partie.trait() + " en " + centre);
                }
                case MI_TEMPS -> {
                    Couleur qui = partie.engagementSecondePeriode();
                    Position centre = caseCentraleLibre(plateau);
                    if (centre == null) { System.out.println("Aucune case centrale libre : on arrête."); return; }
                    partie.engager(qui, centre);
                    System.out.println("Seconde mi-temps : les " + qui + " engagent en " + centre);
                }
                case EN_JEU -> {
                    Couleur trait = partie.trait();
                    ResultatCoup r = partie.jouerTourAdversaire(trait == Couleur.BLEUS ? iaBleus : iaRouges);
                    tours++;
                    if (!r.accepte()) refuses++;
                    System.out.println("Tour " + tours + " : " + trait + " -> " + r);
                    Console.afficher(partie);
                }
                default -> throw new IllegalStateException("statut inattendu : " + partie.statut());
            }
        }
        System.out.println("Match terminé : Bleus " + partie.score().buts(Couleur.BLEUS)
                + " - Rouges " + partie.score().buts(Couleur.ROUGES)
                + ", vainqueur : " + partie.vainqueur().map(Couleur::name).orElse("match nul")
                + " (" + tours + " tours, " + refuses + " coup(s) refusé(s))");
    }

    /** Une équipe complète : dame, deux tours, deux fous, un cavalier, dans cet ordre de cases. */
    static void installer(Plateau plateau, Equipe equipe, List<String> cases) {
        TypePiece[] types = { TypePiece.DAME, TypePiece.TOUR, TypePiece.TOUR,
                              TypePiece.FOU, TypePiece.FOU, TypePiece.CAVALIER };
        for (int i = 0; i < types.length; i++) {
            Piece piece = new Piece(types[i]);
            equipe.ajouter(piece);
            plateau.placer(piece, Position.of(cases.get(i)));
        }
    }

    /** La première des quatre cases centrales sans pièce, ou null. */
    static Position caseCentraleLibre(Plateau plateau) {
        for (String s : List.of("d4", "e4", "d5", "e5")) {
            Position p = Position.of(s);
            if (Partie.estCaseCentrale(p) && !plateau.caseA(p).occupee()) return p;
        }
        return null;
    }
}
