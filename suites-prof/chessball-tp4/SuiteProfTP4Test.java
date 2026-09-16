package chessball;

import chessball.moteur.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static chessball.moteur.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de l'encadrant·e du TP4 : la partie qui se joue, contre l'oracle du
 * dépôt officiel (kit §8 : les 25 coups de départ, perft, vecteurs V1–V3).
 * Compile contre les signatures du sujet ; à copier dans src/test/java2/chessball/.
 */
@DisplayName("TP4 — le moteur qui tourne (règles officielles)")
class SuiteProfTP4Test {

    // ------------------------------------------------------------ outillage

    /** Notation LAN suffixée du dépôt officiel, calculée depuis la géométrie du coup. */
    static String lan(Coup c) {
        Position q = c.origine();
        Position q1 = q.voisine(c.direction()).orElseThrow();
        Position q2 = q1.voisine(c.direction()).orElse(null);
        if (c instanceof Deplacement) return q + "-" + q1;
        if (c instanceof Poussee)     return q + "-" + q1 + "@" + q2;
        if (c instanceof Saut)        return q + "-" + q2 + "^" + q1;
        if (c instanceof Tacle)       return q + "-" + q1 + "!" + q2;
        throw new IllegalArgumentException(c.toString());
    }

    static List<String> lan(List<Coup> coups) { return coups.stream().map(SuiteProfTP4Test::lan).sorted().toList(); }

    /** perft par rejeu depuis la position de départ (pas besoin de copier une partie). */
    static long perft(List<Coup> prefixe, int profondeur) {
        Partie p = Partie.positionOfficielle();
        for (Coup c : prefixe) assertTrue(p.jouer(c).accepte());
        if (profondeur == 0) return 1;
        long total = 0;
        for (Coup c : p.coupsLegaux()) {
            List<Coup> suite = new ArrayList<>(prefixe);
            suite.add(c);
            total += perft(suite, profondeur - 1);
        }
        return total;
    }

    /** Un terrain vide avec deux équipes, pour poser ce qu'on veut. */
    static final class Terrain {
        final Plateau plateau = new Plateau();
        final Equipe bleus = new Equipe(Couleur.BLEUS), rouges = new Equipe(Couleur.ROUGES);
        Piece poser(Couleur c, TypePiece t, String pos) {
            Piece p = new Piece(t);
            (c == Couleur.BLEUS ? bleus : rouges).ajouter(p);
            plateau.placer(p, Position.of(pos));
            return p;
        }
        Partie partie(String ballon) { return new Partie(plateau, bleus, rouges, Position.of(ballon)); }
        Partie partie(String ballon, Couleur trait) { return new Partie(plateau, bleus, rouges, Position.of(ballon), trait); }
    }

    static final List<String> DEPART = Arrays.stream(("b6-a5 b6-a6 b6-b5 b6-c6 c5-b4 c5-b5 c5-c4 c5-c6 "
            + "c5-d4@e3 c5-d5 c5-e3^d4 d6-c6 d6-d5 d6-e6 e5-c3^d4 e5-d4@c3 e5-d5 e5-e4 e5-e6 e5-f4 e5-f5 "
            + "f6-e6 f6-f5 f6-g5 f6-g6").split(" ")).sorted().toList();

    // ------------------------------------------- Q3 : position officielle

    @Test @DisplayName("la position officielle : dix pièces, ballon en d4, Bleus au trait")
    void positionOfficielle() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Statut.EN_JEU, p.statut());
        assertEquals(Couleur.BLEUS, p.trait());
        assertEquals(Position.of("d4"), p.ballon().position());
        assertEquals(5, p.equipe(Couleur.BLEUS).pieces().size());
        assertEquals(5, p.equipe(Couleur.ROUGES).pieces().size());
        assertEquals(TypePiece.DEFENSEUR, p.plateau().pieceEn(Position.of("b6")).orElseThrow().type());
        assertEquals(TypePiece.ATTAQUANT, p.plateau().pieceEn(Position.of("e2")).orElseThrow().type());
        assertEquals(Couleur.ROUGES, p.plateau().pieceEn(Position.of("f1")).orElseThrow().couleur());
        assertTrue(p.memoireTacle().isEmpty());
        assertTrue(p.vainqueur().isEmpty());
    }

    // ------------------------------------------- Q7 : l'oracle officiel

    @Test @DisplayName("les 25 coups légaux de la position de départ (kit §8.1)")
    void vingtCinqCoupsDeDepart() {
        assertEquals(DEPART, lan(Partie.positionOfficielle().coupsLegaux()));
    }

    @Test @DisplayName("perft 1, 2, 3 depuis le départ : 25, 577, 15 224")
    void perftDepart() {
        assertEquals(25, perft(List.of(), 1));
        assertEquals(577, perft(List.of(), 2));
        assertEquals(15_224, perft(List.of(), 3));
    }

    /** V1 : la position d'avant le tacle d5→c4, puis le tacle lui-même. */
    static Partie v1(boolean avecMemoire) {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "b5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "f5");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "f4");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "e3");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "e4");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "b2");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "d2");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "f2");
        if (avecMemoire) {
            t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d5");
            t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "c4");
            Partie p = t.partie("f3", Couleur.BLEUS);
            assertTrue(p.jouer(new Tacle(Position.of("d5"), SUD_OUEST)).accepte());
            return p;
        }
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "c4");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "b3");
        return t.partie("f3", Couleur.ROUGES);
    }

    @Test @DisplayName("V1 : après le tacle, 36 coups, la riposte b3-d5^c4 est absente")
    void v1AvecMemoire() {
        Partie p = v1(true);
        assertEquals(new MemoireTacle(Position.of("c4"), Position.of("b3")), p.memoireTacle().orElseThrow());
        List<String> l = lan(p.coupsLegaux());
        assertEquals(36, l.size());
        assertFalse(l.contains("b3-d5^c4"));
        assertEquals(Refus.REPRESAILLES, p.verifierSaut(new Saut(Position.of("b3"), NORD_EST)));
    }

    @Test @DisplayName("V1 : même position sans mémoire, 37 coups")
    void v1SansMemoire() {
        List<String> l = lan(v1(false).coupsLegaux());
        assertEquals(37, l.size());
        assertTrue(l.contains("b3-d5^c4"));
    }

    static Partie v2() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "b6");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d6");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "c5");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "e5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "c3");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "c2");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "e2");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "b1");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "d1");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "f1");
        return t.partie("b3");
    }

    @Test @DisplayName("V2 : zones de touche, 27 coups, aucune poussée, mais le tacle c3-c2!c1")
    void v2ZonesDeTouche() {
        Partie p = v2();
        List<String> l = lan(p.coupsLegaux());
        assertEquals(27, l.size());
        assertTrue(l.stream().noneMatch(s -> s.contains("@")));
        assertTrue(l.contains("c3-c2!c1"), "repousser un adversaire sur sa propre ligne de but est légal");
        assertEquals(Refus.ZONE_DE_TOUCHE, p.verifierPoussee(new Poussee(Position.of("c3"), OUEST)));
    }

    static Partie v3() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "b6");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d6");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "c5");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "e5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "e3");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "c2");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "e2");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "b1");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "d1");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "f1");
        return t.partie("f2");
    }

    @Test @DisplayName("V3 : but dans le coin, 28 coups dont e3-f2@g1")
    void v3ButDansLeCoin() {
        Partie p = v3();
        List<String> l = lan(p.coupsLegaux());
        assertEquals(28, l.size());
        assertTrue(l.contains("e3-f2@g1"));
        ResultatCoup r = p.jouer(new Poussee(Position.of("e3"), SUD_EST));
        assertTrue(r.accepte());
        assertTrue(r.but());
        assertEquals(Statut.TERMINE, p.statut());
        assertEquals(Couleur.BLEUS, p.vainqueur().orElseThrow());
        assertTrue(p.coupsLegaux().isEmpty());
        assertEquals(Refus.PARTIE_TERMINEE, p.verifier(new Deplacement(Position.of("b1"), NORD)));
    }

    // ------------------------------------------- Q4–Q6 : chaque refus, chaque effet

    @Test void pasDePieceEtPasSonTour() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Refus.PAS_DE_PIECE, p.verifier(new Deplacement(Position.of("a1"), NORD)));
        assertEquals(Refus.PAS_SON_TOUR, p.verifier(new Deplacement(Position.of("c2"), NORD)));
        assertEquals(Refus.PAS_SON_TOUR, p.jouer(new Deplacement(Position.of("c2"), NORD)).refus());
    }

    @Test void deplacementHorsPlateauEtCaseNonLibre() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Refus.HORS_PLATEAU, p.verifierDeplacement(new Deplacement(Position.of("d6"), NORD)));
        assertEquals(Refus.CASE_NON_LIBRE, p.verifierDeplacement(new Deplacement(Position.of("b6"), SUD_EST)), "c5 est occupée");
        assertEquals(Refus.CASE_NON_LIBRE, p.verifierDeplacement(new Deplacement(Position.of("c5"), SUD_EST)), "la case du ballon n'est pas libre");
        assertEquals(Refus.AUCUN, p.verifierDeplacement(new Deplacement(Position.of("d6"), SUD)));
    }
    @Test void unDeplacementAccepteTermineLeTour() {
        Partie p = Partie.positionOfficielle();
        ResultatCoup r = p.jouer(new Deplacement(Position.of("d6"), SUD));
        assertTrue(r.accepte()); assertFalse(r.but());
        assertEquals(Couleur.ROUGES, p.trait());
        assertTrue(p.plateau().pieceEn(Position.of("d5")).isPresent());
        assertTrue(p.plateau().estLibre(Position.of("d6")));
    }

    @Test void unCoupRefuseNeChangeRien() {
        Partie p = Partie.positionOfficielle();
        assertFalse(p.jouer(new Deplacement(Position.of("d6"), NORD)).accepte());
        assertEquals(Couleur.BLEUS, p.trait());
        assertEquals(DEPART, lan(p.coupsLegaux()));
    }

    @Test void pousseeNominale() {
        Partie p = Partie.positionOfficielle();
        ResultatCoup r = p.jouer(new Poussee(Position.of("c5"), SUD_EST));      // c5-d4@e3
        assertTrue(r.accepte()); assertFalse(r.but());
        assertEquals(Position.of("e3"), p.ballon().position());
        assertTrue(p.plateau().caseA(Position.of("e3")).porteBallon());
        assertFalse(p.plateau().caseA(Position.of("d4")).porteBallon());
        assertEquals(TypePiece.ATTAQUANT, p.plateau().pieceEn(Position.of("d4")).orElseThrow().type());
        assertEquals(Couleur.ROUGES, p.trait());
    }

    @Test void pousseeRefusee() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Refus.PAS_DE_BALLON, p.verifierPoussee(new Poussee(Position.of("c5"), NORD)));
        assertEquals(Refus.PAS_DE_BALLON, p.verifierPoussee(new Poussee(Position.of("b6"), NORD)), "hors plateau compte comme pas de ballon");
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "c3");
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "e4");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "f1");
        Partie q = t.partie("b3");
        assertEquals(Refus.ZONE_DE_TOUCHE, q.verifierPoussee(new Poussee(Position.of("c3"), OUEST)));
        Terrain t2 = new Terrain();
        t2.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "c3");
        t2.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "e3");
        Partie q2 = t2.partie("d3");
        assertEquals(Refus.CASE_NON_LIBRE, q2.verifierPoussee(new Poussee(Position.of("c3"), EST)));
        Terrain t3 = new Terrain();
        t3.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "f6");
        Partie q3 = t3.partie("g6");
        assertEquals(Refus.HORS_PLATEAU, q3.verifierPoussee(new Poussee(Position.of("f6"), EST)));
    }

    @Test void butContreSonCamp() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d4");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "b1");
        Partie p = t.partie("d5");
        ResultatCoup r = p.jouer(new Poussee(Position.of("d4"), NORD));
        assertTrue(r.but());
        assertEquals(Couleur.ROUGES, p.vainqueur().orElseThrow(), "le ballon sur le rang 6 : les Rouges gagnent, qui que soit le pousseur");
        assertEquals(Statut.TERMINE, p.statut());
    }

    @Test void tacleNominalEtMemoire() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d5");
        Piece victime = t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "d4");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "a1");
        Partie p = t.partie("g6");
        ResultatCoup r = p.jouer(new Tacle(Position.of("d5"), SUD));
        assertTrue(r.accepte());
        assertEquals(Position.of("d3"), p.plateau().positionDe(victime).orElseThrow());
        assertTrue(p.plateau().pieceEn(Position.of("d4")).orElseThrow().couleur() == Couleur.BLEUS);
        assertEquals(Position.of("g6"), p.ballon().position(), "le ballon ne bouge pas dans un tacle");
        assertEquals(new MemoireTacle(Position.of("d4"), Position.of("d3")), p.memoireTacle().orElseThrow());
        assertEquals(Refus.REPRESAILLES, p.verifierTacle(new Tacle(Position.of("d3"), NORD)));
        assertTrue(p.jouer(new Deplacement(Position.of("a1"), NORD)).accepte());
        assertTrue(p.memoireTacle().isEmpty(), "tout autre coup efface la mémoire");
    }

    @Test void tacleRefuse() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "c5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "e5");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "b1");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "e4");
        t.poser(Couleur.ROUGES, TypePiece.ATTAQUANT, "a1");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "f3");
        Partie p = t.partie("g6");
        assertEquals(Refus.RESERVE_AUX_DEFENSEURS, p.verifierTacle(new Tacle(Position.of("c5"), SUD)));
        assertEquals(Refus.PAS_D_ADVERSAIRE, p.verifierTacle(new Tacle(Position.of("d5"), EST)), "une pièce de son camp");
        assertEquals(Refus.PAS_D_ADVERSAIRE, p.verifierTacle(new Tacle(Position.of("d5"), NORD)), "une case vide");
        assertEquals(Refus.CASE_NON_LIBRE, p.verifierTacle(new Tacle(Position.of("d5"), SUD_EST)), "e4 adverse, f3 occupée");
        assertEquals(Refus.HORS_PLATEAU, p.verifierTacle(new Tacle(Position.of("b1"), OUEST)));
    }

    @Test void sautNominalParDessusLeBallonOuUnePiece() {
        Partie p = Partie.positionOfficielle();
        ResultatCoup r = p.jouer(new Saut(Position.of("e5"), SUD_OUEST));          // e5-c3^d4 : par-dessus le ballon
        assertTrue(r.accepte());
        assertTrue(p.plateau().pieceEn(Position.of("c3")).isPresent());
        assertEquals(Position.of("d4"), p.ballon().position(), "ce qui est sauté ne bouge pas");
        assertEquals(Couleur.ROUGES, p.trait());
    }

    @Test void sautRefuse() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Refus.RESERVE_AUX_ATTAQUANTS, p.verifierSaut(new Saut(Position.of("d6"), SUD)));
        assertEquals(Refus.RIEN_A_SAUTER, p.verifierSaut(new Saut(Position.of("c5"), SUD)));
        assertEquals(Refus.HORS_PLATEAU, p.verifierSaut(new Saut(Position.of("c5"), NORD_EST)), "d6 occupée, e7 hors plateau");
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.ATTAQUANT, "c4");
        t.poser(Couleur.BLEUS, TypePiece.DEFENSEUR, "d4");
        t.poser(Couleur.ROUGES, TypePiece.DEFENSEUR, "e4");
        Partie q = t.partie("g6");
        assertEquals(Refus.CASE_NON_LIBRE, q.verifierSaut(new Saut(Position.of("c4"), EST)));
    }

    @Test void verifierAiguilleParType() {
        Partie p = Partie.positionOfficielle();
        assertEquals(Refus.AUCUN, p.verifier(new Poussee(Position.of("c5"), SUD_EST)));
        assertEquals(Refus.AUCUN, p.verifier(new Saut(Position.of("e5"), SUD_OUEST)));
        assertEquals(Refus.PAS_DE_BALLON, p.verifier(new Poussee(Position.of("c5"), NORD)));
        assertEquals(Refus.RESERVE_AUX_DEFENSEURS, p.verifier(new Tacle(Position.of("c5"), SUD)));
    }

    // ------------------------------------------- l'adversaire (contrat, Q4)

    @Test void unAdversaireQuiRenvoieUnCoupIllegalEstRefuseProprement() {
        Partie p = Partie.positionOfficielle();
        StrategieAdversaire tricheur = partie -> new Deplacement(Position.of("d6"), NORD);
        ResultatCoup r = p.jouerTourAdversaire(tricheur);
        assertFalse(r.accepte());
        assertEquals(Refus.HORS_PLATEAU, r.refus());
        assertEquals(Couleur.BLEUS, p.trait());
    }

    @Test void unAdversaireQuiJoueLePremierCoupLegalFaitAvancerLaPartie() {
        Partie p = Partie.positionOfficielle();
        StrategieAdversaire premier = partie -> partie.coupsLegaux().get(0);
        assertTrue(p.jouerTourAdversaire(premier).accepte());
        assertEquals(Couleur.ROUGES, p.trait());
    }

    // ------------------------------------------- deux scénarios en style BDD

    @Nested @DisplayName("Étant donné un attaquant bleu en c5 et le ballon en d4")
    class PousseeVersLeBut {
        Partie partie;
        @BeforeEach void etantDonne() { partie = Partie.positionOfficielle(); }

        @Test @DisplayName("quand les Bleus poussent c5-d4@e3, alors le ballon est en e3 et l'attaquant en d4")
        void pousseeAcceptee() {
            assertTrue(partie.jouer(new Poussee(Position.of("c5"), SUD_EST)).accepte());
            assertEquals(Position.of("e3"), partie.ballon().position());
            assertTrue(partie.plateau().pieceEn(Position.of("d4")).isPresent());
        }

        @Test @DisplayName("… et le trait passe aux Rouges")
        void traitAdverse() {
            partie.jouer(new Poussee(Position.of("c5"), SUD_EST));
            assertEquals(Couleur.ROUGES, partie.trait());
        }
    }

    @Nested @DisplayName("Étant donné un défenseur bleu en c3 et le ballon en b3, contre le bord")
    class PousseeEnZoneDeTouche {
        Partie partie;
        @BeforeEach void etantDonne() { partie = v2(); }

        @Test @DisplayName("quand les Bleus tentent c3-b3@a3, alors le coup est refusé : zone de touche")
        void refusee() {
            ResultatCoup r = partie.jouer(new Poussee(Position.of("c3"), OUEST));
            assertFalse(r.accepte());
            assertEquals(Refus.ZONE_DE_TOUCHE, r.refus());
        }

        @Test @DisplayName("… et rien n'a bougé : ballon en b3, trait aux Bleus")
        void rienNaBouge() {
            partie.jouer(new Poussee(Position.of("c3"), OUEST));
            assertEquals(Position.of("b3"), partie.ballon().position());
            assertEquals(Couleur.BLEUS, partie.trait());
        }
    }
}
