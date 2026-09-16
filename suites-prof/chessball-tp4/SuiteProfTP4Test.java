package chessball;

import chessball.moteur.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de l'encadrant·e du TP4 (Q5) : la partie qui se joue. Compile contre
 * les signatures du sujet ; se copie dans src/test/java2/chessball/.
 */
@DisplayName("TP4 — le moteur qui tourne")
class SuiteProfTP4Test {

    /** Fabrique de fixtures : un plateau, deux équipes. */
    static final class Terrain {
        final Plateau plateau = new Plateau();
        final Equipe bleus = new Equipe(Couleur.BLEUS), rouges = new Equipe(Couleur.ROUGES);
        Partie partie;
        Ballon ballon;
        Piece poser(Couleur c, TypePiece t, String pos) {
            Piece p = new Piece(t);
            (c == Couleur.BLEUS ? bleus : rouges).ajouter(p);
            plateau.placer(p, Position.of(pos));
            return p;
        }
        Partie demarrer(int k, Couleur engage) { return demarrer(k, engage, "d4"); }
        Partie demarrer(int k, Couleur engage, String caseCentrale) {
            partie = new Partie(plateau, bleus, rouges, k);
            partie.engager(engage, Position.of(caseCentrale));
            ballon = partie.ballon().orElseThrow();
            return partie;
        }
        Terrain porteuse(Piece p) { ballon.prendrePossession(p); return this; }
    }

    // =====================================================================
    // Q1 : engagement
    // =====================================================================

    @Test @DisplayName("l'engagement n'est légal que sur d4, d5, e4 ou e5")
    void engagementSurCaseCentrale() {
        Terrain t = new Terrain();
        Partie p = new Partie(t.plateau, t.bleus, t.rouges, 20);
        assertEquals(Statut.ENGAGEMENT, p.statut());
        assertTrue(Partie.estCaseCentrale(Position.of("d4")));
        assertFalse(Partie.estCaseCentrale(Position.of("a1")));
        assertThrows(IllegalArgumentException.class, () -> p.engager(Couleur.BLEUS, Position.of("a1")));
        assertDoesNotThrow(() -> p.engager(Couleur.BLEUS, Position.of("e5")));
        assertEquals(Statut.EN_JEU, p.statut());
        assertEquals(Couleur.BLEUS, p.trait());
        assertEquals(1, p.periode());
        assertTrue(p.ballon().orElseThrow().estLibre());
        assertEquals(Position.of("e5"), p.ballon().orElseThrow().position());
    }

    @Test @DisplayName("une case centrale occupée refuse l'engagement")
    void engagementCaseOccupee() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.TOUR, "d4");
        Partie p = new Partie(t.plateau, t.bleus, t.rouges, 20);
        assertThrows(IllegalStateException.class, () -> p.engager(Couleur.BLEUS, Position.of("d4")));
    }

    @Test @DisplayName("on n'engage pas une partie en jeu")
    void pasDEngagementEnJeu() {
        Terrain t = new Terrain();
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertThrows(IllegalStateException.class, () -> p.engager(Couleur.ROUGES, Position.of("e5")));
    }

    // =====================================================================
    // Q2 : déplacement et fin de tour
    // =====================================================================

    @Test @DisplayName("la tour glisse en ligne, pas en diagonale")
    void tourGlisseEnLigne() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertEquals(Refus.AUCUN, p.verifierDeplacement(new Deplacement(Position.of("b2"), Position.of("b6"))));
        assertEquals(Refus.AUCUN, p.verifierDeplacement(new Deplacement(Position.of("b2"), Position.of("g2"))));
        assertEquals(Refus.NON_ALIGNEE, p.verifierDeplacement(new Deplacement(Position.of("b2"), Position.of("c3"))));
    }

    @Test @DisplayName("une pièce intermédiaire bloque la tour, pas le cavalier")
    void obstruction() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
        t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "b1");
        t.poser(Couleur.ROUGES, TypePiece.FOU, "b4");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertEquals(Refus.TRAJECTOIRE_OBSTRUEE,
                p.verifierDeplacement(new Deplacement(Position.of("b2"), Position.of("b6"))));
        t.plateau.placer(t.plateau.pieceEn(Position.of("b4")).orElseThrow(), Position.of("c2"));
        assertEquals(Refus.AUCUN,
                p.verifierDeplacement(new Deplacement(Position.of("b1"), Position.of("c3"))), "le cavalier saute");
    }

    @Test @DisplayName("pas de capture : case d'arrivée occupée refusée")
    void caseArriveeOccupee() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
        t.poser(Couleur.ROUGES, TypePiece.FOU, "b6");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertEquals(Refus.CASE_OCCUPEE,
                p.verifierDeplacement(new Deplacement(Position.of("b2"), Position.of("b6"))));
    }

    @Test @DisplayName("un coup accepté change le trait ; un coup refusé ne consomme pas le tour")
    void finDeTour() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        ResultatCoup refuse = p.jouer(new Deplacement(Position.of("b2"), Position.of("c3")));
        assertFalse(refuse.accepte());
        assertEquals(Refus.NON_ALIGNEE, refuse.refus());
        assertEquals(Couleur.BLEUS, p.trait());
        ResultatCoup ok = p.jouer(new Deplacement(Position.of("b2"), Position.of("b6")));
        assertTrue(ok.accepte());
        assertFalse(ok.but());
        assertEquals(Couleur.ROUGES, p.trait());
        assertEquals(Position.of("b6"), t.plateau.positionDe(t.plateau.pieceEn(Position.of("b6")).orElseThrow()).orElseThrow());
    }

    @Test @DisplayName("ce n'est pas son tour")
    void pasSonTour() {
        Terrain t = new Terrain();
        t.poser(Couleur.ROUGES, TypePiece.TOUR, "h8");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        ResultatCoup r = p.jouer(new Deplacement(Position.of("h8"), Position.of("h5")));
        assertFalse(r.accepte());
        assertEquals(Refus.PAS_SON_TOUR, r.refus());
    }

    @Test @DisplayName("pas de pièce à l'origine")
    void pasDePiece() {
        Terrain t = new Terrain();
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertEquals(Refus.PAS_DE_PIECE, p.jouer(new Deplacement(Position.of("a1"), Position.of("a2"))).refus());
    }

    @Test @DisplayName("une pièce qui atteint la case du ballon libre s'en empare")
    void priseDePossessionParDeplacement() {
        Terrain t = new Terrain();
        Piece tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "d1");
        Partie p = t.demarrer(20, Couleur.BLEUS);          // ballon libre en d4
        assertTrue(p.ballon().orElseThrow().estLibre());
        ResultatCoup r = p.jouer(new Deplacement(Position.of("d1"), Position.of("d4")));
        assertTrue(r.accepte());
        assertSame(tour, p.ballon().orElseThrow().porteuse().orElseThrow());
    }

    // =====================================================================
    // Q3 : passe, style BDD (un scénario = une classe @Nested, un alors = un test)
    // =====================================================================

    @Nested
    @DisplayName("Étant donné une tour bleue porteuse en b2 et un fou bleu en b6")
    class PasseAlignee {
        Terrain t; Piece tour, fou;

        @BeforeEach void etantDonne() {
            t = new Terrain();
            tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
            fou  = t.poser(Couleur.BLEUS, TypePiece.FOU,  "b6");
            t.demarrer(20, Couleur.BLEUS);
            t.porteuse(tour);
        }

        @Test @DisplayName("quand les Bleus passent de b2 à b6, alors le fou devient porteur")
        void passeRecue() {
            ResultatCoup r = t.partie.jouer(new Passe(Position.of("b2"), Position.of("b6")));
            assertTrue(r.accepte());
            assertSame(fou, t.ballon.porteuse().orElseThrow());
        }

        @Test @DisplayName("… et le trait passe aux Rouges")
        void traitAdverse() {
            t.partie.jouer(new Passe(Position.of("b2"), Position.of("b6")));
            assertEquals(Couleur.ROUGES, t.partie.trait());
        }

        @Test @DisplayName("… et aucune pièce n'a bougé")
        void rienNeBouge() {
            t.partie.jouer(new Passe(Position.of("b2"), Position.of("b6")));
            assertSame(tour, t.plateau.pieceEn(Position.of("b2")).orElseThrow());
            assertSame(fou, t.plateau.pieceEn(Position.of("b6")).orElseThrow());
        }
    }

    @Nested
    @DisplayName("Étant donné une tour bleue porteuse en b2, un fou bleu en b6 et un cavalier rouge en b4")
    class PasseObstruee {
        Terrain t; Piece tour;

        @BeforeEach void etantDonne() {
            t = new Terrain();
            tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
            t.poser(Couleur.BLEUS, TypePiece.FOU, "b6");
            t.poser(Couleur.ROUGES, TypePiece.CAVALIER, "b4");
            t.demarrer(20, Couleur.BLEUS);
            t.porteuse(tour);
        }

        @Test @DisplayName("quand les Bleus tentent la passe b2 → b6, alors elle est refusée (obstruée, réponse n° 7)")
        void refusee() {
            ResultatCoup r = t.partie.jouer(new Passe(Position.of("b2"), Position.of("b6")));
            assertFalse(r.accepte());
            assertEquals(Refus.TRAJECTOIRE_OBSTRUEE, r.refus());
        }

        @Test @DisplayName("… et la tour reste porteuse, le trait reste aux Bleus")
        void rienNeChange() {
            t.partie.jouer(new Passe(Position.of("b2"), Position.of("b6")));
            assertSame(tour, t.ballon.porteuse().orElseThrow());
            assertEquals(Couleur.BLEUS, t.partie.trait());
        }
    }

    /** La table de décision du TP5 Q3, en avant-goût : chaque ligne un motif de refus. */
    @ParameterizedTest(name = "passe {0} → {1} : {2}")
    @CsvSource({
        "b2, b6, AUCUN",
        "b2, c4, NON_ALIGNEE",
        "b2, g7, CIBLE_NON_COEQUIPIERE",
        "b2, e2, CIBLE_NON_COEQUIPIERE",
        "b6, b2, PAS_PORTEUSE",
    })
    void motifsDeRefusDUnePasse(String de, String vers, Refus attendu) {
        Terrain t = new Terrain();
        Piece tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "b2");
        t.poser(Couleur.BLEUS, TypePiece.FOU, "b6");
        t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "c4");
        t.poser(Couleur.ROUGES, TypePiece.DAME, "g7");
        t.demarrer(20, Couleur.BLEUS);
        t.porteuse(tour);
        assertEquals(attendu, t.partie.verifierPasse(new Passe(Position.of(de), Position.of(vers))));
    }

    // =====================================================================
    // Q4 : tir, but, mi-temps, fin de match (réponses client n° 8, 11, 12, 13)
    // =====================================================================

    @Test @DisplayName("un tir sans obstacle marque un but, et l'équipe qui encaisse engage")
    void tirSansObstacle() {
        Terrain t = new Terrain();
        Piece dame = t.poser(Couleur.BLEUS, TypePiece.DAME, "d6");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        t.porteuse(dame);
        ResultatCoup r = p.jouer(new Tir(Position.of("d6"), Direction.NORD));
        assertTrue(r.accepte());
        assertTrue(r.but());
        assertEquals(1, p.score().buts(Couleur.BLEUS));
        assertEquals(0, p.score().buts(Couleur.ROUGES));
        assertEquals(Statut.ENGAGEMENT, p.statut());
        assertEquals(Couleur.ROUGES, p.trait(), "l'équipe qui encaisse engage (réponse n° 12)");
    }

    @Test @DisplayName("un tir est intercepté par la première pièce rencontrée (adverse)")
    void tirIntercepte() {
        Terrain t = new Terrain();
        Piece dame  = t.poser(Couleur.BLEUS, TypePiece.DAME, "d4");
        Piece tourR = t.poser(Couleur.ROUGES, TypePiece.TOUR, "d6");
        Partie p = t.demarrer(20, Couleur.BLEUS, "e5");   // d4 est occupée
        t.porteuse(dame);
        ResultatCoup r = p.jouer(new Tir(Position.of("d4"), Direction.NORD));
        assertTrue(r.accepte());
        assertFalse(r.but());
        assertSame(tourR, p.ballon().orElseThrow().porteuse().orElseThrow());
        assertEquals(0, p.score().buts(Couleur.BLEUS));
        assertEquals(Couleur.ROUGES, p.trait());
    }

    @Test @DisplayName("un tir arrêté par une coéquipière est une simple réception")
    void tirRecuParUneCoequipiere() {
        Terrain t = new Terrain();
        Piece dame = t.poser(Couleur.BLEUS, TypePiece.DAME, "d4");
        Piece fou  = t.poser(Couleur.BLEUS, TypePiece.FOU, "d7");
        Partie p = t.demarrer(20, Couleur.BLEUS, "e5");
        t.porteuse(dame);
        ResultatCoup r = p.jouer(new Tir(Position.of("d4"), Direction.NORD));
        assertTrue(r.accepte());
        assertFalse(r.but());
        assertSame(fou, p.ballon().orElseThrow().porteuse().orElseThrow());
    }

    @Test @DisplayName("sortie latérale : le ballon s'arrête libre sur la dernière case")
    void sortieLaterale() {
        Terrain t = new Terrain();
        Piece tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "d4");
        Partie p = t.demarrer(20, Couleur.BLEUS, "e5");
        t.porteuse(tour);
        ResultatCoup r = p.jouer(new Tir(Position.of("d4"), Direction.OUEST));
        assertTrue(r.accepte());
        assertFalse(r.but());
        assertTrue(p.ballon().orElseThrow().estLibre());
        assertEquals(Position.of("a4"), p.ballon().orElseThrow().position());
    }

    @Test @DisplayName("un cavalier ne peut pas tirer en ligne")
    void cavalierNeTirePas() {
        Terrain t = new Terrain();
        Piece cav = t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "d4");
        Partie p = t.demarrer(20, Couleur.BLEUS, "e5");
        t.porteuse(cav);
        ResultatCoup r = p.jouer(new Tir(Position.of("d4"), Direction.NORD));
        assertFalse(r.accepte());
        assertEquals(Refus.DIRECTION_IMPOSSIBLE, r.refus());
    }

    @Test @DisplayName("on ne tire pas sans porter le ballon")
    void tirSansBallon() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.DAME, "d6");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        assertEquals(Refus.PAS_PORTEUSE, p.jouer(new Tir(Position.of("d6"), Direction.NORD)).refus());
    }

    @Test @DisplayName("AMBIGUÏTÉ jamais tranchée par le client : tirer vers SA PROPRE ligne de fond marque contre son camp")
    void butContreSonCamp() {
        Terrain t = new Terrain();
        Piece tour = t.poser(Couleur.BLEUS, TypePiece.TOUR, "d4");
        Partie p = t.demarrer(20, Couleur.BLEUS, "e5");
        t.porteuse(tour);
        ResultatCoup r = p.jouer(new Tir(Position.of("d4"), Direction.SUD));
        assertTrue(r.but());
        assertEquals(1, p.score().buts(Couleur.ROUGES),
                "le document client ne tranche pas ce cas — décision de conception à faire valider");
    }

    @Test @DisplayName("le match se termine sur un nul quand K tours sont joués deux fois")
    void matchNul() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "b1");
        t.poser(Couleur.ROUGES, TypePiece.CAVALIER, "b8");
        Partie p = t.demarrer(1, Couleur.BLEUS);
        assertThrows(IllegalStateException.class, p::vainqueur, "match en cours");
        p.jouer(new Deplacement(Position.of("b1"), Position.of("c3")));
        p.jouer(new Deplacement(Position.of("b8"), Position.of("c6")));
        assertEquals(Statut.MI_TEMPS, p.statut());
        assertEquals(2, p.periode());
        assertEquals(Couleur.ROUGES, p.engagementSecondePeriode());
        p.engager(Couleur.ROUGES, Position.of("e5"));
        p.jouer(new Deplacement(Position.of("c6"), Position.of("b8")));
        p.jouer(new Deplacement(Position.of("c3"), Position.of("b1")));
        assertEquals(Statut.TERMINE, p.statut());
        assertTrue(p.vainqueur().isEmpty(), "match nul autorisé (réponse n° 13)");
        assertEquals(Refus.PARTIE_TERMINEE, p.jouer(new Deplacement(Position.of("b1"), Position.of("c3"))).refus());
    }

    @Test @DisplayName("le vainqueur est l'équipe qui a marqué le plus de buts")
    void vainqueur() {
        Terrain t = new Terrain();
        Piece dame = t.poser(Couleur.BLEUS, TypePiece.DAME, "d6");
        t.poser(Couleur.ROUGES, TypePiece.CAVALIER, "h8");
        Partie p = t.demarrer(1, Couleur.BLEUS);
        t.porteuse(dame);
        assertTrue(p.jouer(new Tir(Position.of("d6"), Direction.NORD)).but());   // 1 tour joué
        p.engager(Couleur.ROUGES, Position.of("d4"));
        p.jouer(new Deplacement(Position.of("h8"), Position.of("g6")));           // 2 tours : mi-temps
        assertEquals(Statut.MI_TEMPS, p.statut());
        p.engager(p.engagementSecondePeriode(), Position.of("e5"));
        p.jouer(new Deplacement(Position.of("g6"), Position.of("h8")));
        p.jouer(new Deplacement(Position.of("d6"), Position.of("d7")));
        assertEquals(Statut.TERMINE, p.statut());
        assertEquals(Couleur.BLEUS, p.vainqueur().orElseThrow());
    }

    // =====================================================================
    // Q6 : l'adversaire automatique (contrat de StrategieAdversaire)
    // =====================================================================

    @Test @DisplayName("la partie demande un coup à la stratégie et l'applique")
    void tourAdversaire() {
        Terrain t = new Terrain();
        t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "b1");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        StrategieAdversaire ia = partie -> new Deplacement(Position.of("b1"), Position.of("c3"));
        ResultatCoup r = p.jouerTourAdversaire(ia);
        assertTrue(r.accepte());
        assertEquals(Couleur.ROUGES, p.trait());
        assertTrue(t.plateau.pieceEn(Position.of("c3")).isPresent());
    }

    @Test @DisplayName("un coup illégal de la stratégie est refusé proprement, rien ne bouge")
    void coupIllegalDeLaStrategie() {
        Terrain t = new Terrain();
        Piece cav = t.poser(Couleur.BLEUS, TypePiece.CAVALIER, "b1");
        Partie p = t.demarrer(20, Couleur.BLEUS);
        StrategieAdversaire ia = partie -> new Deplacement(Position.of("b1"), Position.of("b5"));
        ResultatCoup r = p.jouerTourAdversaire(ia);
        assertFalse(r.accepte());
        assertEquals(Refus.NON_ALIGNEE, r.refus());
        assertEquals(Couleur.BLEUS, p.trait());
        assertSame(cav, t.plateau.pieceEn(Position.of("b1")).orElseThrow());
        assertEquals(Statut.EN_JEU, p.statut());
    }
}
