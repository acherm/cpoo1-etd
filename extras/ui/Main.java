package chessball.ui;

import chessball.moteur.Coup;
import chessball.moteur.Couleur;
import chessball.moteur.Partie;
import chessball.moteur.ResultatCoup;
import chessball.moteur.Statut;
import chessball.moteur.StrategieAdversaire;
import chessball.moteur.StrategieAleatoire;

import java.util.Random;

/**
 * TP4 : deux IA aléatoires jouent une partie complète dans la console, depuis
 * la position officielle. Lancement : {@code mvn -q compile exec:java}.
 */
public final class Main {
    public static void main(String[] args) {
        Partie partie = Partie.positionOfficielle();
        StrategieAdversaire iaBleus = new StrategieAleatoire(new Random());
        StrategieAdversaire iaRouges = new StrategieAleatoire(new Random());

        System.out.println("Position de départ, les Bleus jouent en premier");
        Console.afficher(partie);

        int coups = 0, refuses = 0;
        while (partie.statut() == Statut.EN_JEU) {
            Couleur trait = partie.trait();
            StrategieAdversaire ia = trait == Couleur.BLEUS ? iaBleus : iaRouges;
            Coup coup = ia.choisirCoup(partie);
            ResultatCoup r = partie.jouer(coup);
            coups++;
            if (!r.accepte()) refuses++;
            System.out.println("Coup " + coups + " : " + trait + " joue " + coup + " -> " + r);
            Console.afficher(partie);
        }
        System.out.println("Partie terminée en " + coups + " coups (" + refuses + " refusé(s)) : "
                + partie.vainqueur().map(c -> "victoire des " + c).orElse("nulle"));
    }
}
