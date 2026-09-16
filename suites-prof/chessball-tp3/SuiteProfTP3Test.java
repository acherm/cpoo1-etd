package chessball;

import chessball.moteur.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de l'encadrant·e du TP3 (Q10) : le moteur statique. Compile contre
 * les signatures du sujet ; se copie dans src/test/java2/chessball/.
 */
@DisplayName("TP3 — du modèle du TD2 au code")
class SuiteProfTP3Test {

    // ---- Q3 : Position ---------------------------------------------------
    @Test void positionValide() { assertEquals(new Position(1, 1), Position.of("b2")); }
    @Test void positionCoins()  { assertEquals("a1", Position.of("a1").toString());
                                  assertEquals("h8", Position.of("h8").toString()); }
    @Test void positionHorsPlateau() {
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
        assertThrows(IllegalArgumentException.class, () -> Position.of("i1"));
        assertThrows(IllegalArgumentException.class, () -> Position.of("a9"));
        assertThrows(IllegalArgumentException.class, () -> Position.of("b22"));
    }
    @Test void egaliteStructurelle() {
        assertEquals(Position.of("b2"), new Position(1, 1));
        assertEquals(Position.of("b2").hashCode(), new Position(1, 1).hashCode());
        assertNotSame(Position.of("b2"), new Position(1, 1));
    }

    // ---- Q4 : Coup ---------------------------------------------------------
    @Test void unSwitchSurCoupDoitAvoirUnDefault() {
        Coup c = new Passe(Position.of("b2"), Position.of("b6"));
        String nom = switch (c) {                       // Coup n'est pas sealed : default obligatoire
            case Deplacement d -> "deplacement";
            case Passe p       -> "passe";
            case Tir t         -> "tir";
            default -> throw new IllegalStateException("type de coup inconnu");
        };
        assertEquals("passe", nom);
        assertEquals(Position.of("b2"), c.origine());
    }

    // ---- Q5/Q6 : intégrité référentielle et encapsulation -----------------
    @Test void ajouterEtablitLesDeuxSens() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        Piece tour = new Piece(TypePiece.TOUR);
        assertNull(tour.equipe());
        bleus.ajouter(tour);
        assertSame(bleus, tour.equipe());
        assertEquals(Couleur.BLEUS, tour.couleur());
        assertTrue(bleus.pieces().contains(tour));
    }
    @Test void ajoutEnDoubleEstIdempotent() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        Piece tour = new Piece(TypePiece.TOUR);
        bleus.ajouter(tour); bleus.ajouter(tour);
        assertEquals(1, bleus.pieces().size());
    }
    @Test void septiemePieceRefusee() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        for (int i = 0; i < Equipe.EFFECTIF; i++) bleus.ajouter(new Piece(TypePiece.TOUR));
        assertThrows(IllegalStateException.class, () -> bleus.ajouter(new Piece(TypePiece.FOU)));
    }
    @Test void pieceDejaEngageeAilleursRefusee() {
        Equipe bleus = new Equipe(Couleur.BLEUS), rouges = new Equipe(Couleur.ROUGES);
        Piece p = new Piece(TypePiece.DAME);
        bleus.ajouter(p);
        assertThrows(IllegalStateException.class, () -> rouges.ajouter(p));
    }
    @Test void laVueExposeeEstNonModifiable() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        bleus.ajouter(new Piece(TypePiece.TOUR));
        assertThrows(UnsupportedOperationException.class, () -> bleus.pieces().clear());
    }

    // ---- Q7 : Plateau, composition ---------------------------------------
    @Test void plateauCreeSes64Cases() {
        Plateau p = new Plateau();
        assertNotNull(p.caseA(Position.of("a1")));
        assertNotNull(p.caseA(Position.of("h8")));
        assertEquals(Position.of("h8"), p.caseA(Position.of("h8")).position());
        assertTrue(p.pieceEn(Position.of("d4")).isEmpty());
        assertFalse(p.caseA(Position.of("d4")).occupee());
    }
    @Test void placerPuisRelire() {
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("b2"));
        assertSame(tour, p.pieceEn(Position.of("b2")).orElseThrow());
        assertSame(tour, p.caseA(Position.of("b2")).piece().orElseThrow());
        assertEquals(Position.of("b2"), p.positionDe(tour).orElseThrow());
    }
    @Test void caseOccupeeRefusee() {
        Plateau p = new Plateau();
        p.placer(new Piece(TypePiece.TOUR), Position.of("b2"));
        assertThrows(IllegalStateException.class,
                () -> p.placer(new Piece(TypePiece.FOU), Position.of("b2")));
    }
    @Test void deplacerLibereLAncienneCase() {
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("b2"));
        p.placer(tour, Position.of("b6"));
        assertTrue(p.pieceEn(Position.of("b2")).isEmpty());
        assertEquals(Position.of("b6"), p.positionDe(tour).orElseThrow());
    }
    @Test void retirerVideLaCase() {
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("b2"));
        p.retirer(tour);
        assertTrue(p.pieceEn(Position.of("b2")).isEmpty());
        assertTrue(p.positionDe(tour).isEmpty());
    }

    // ---- Q8 : le ballon, invariant XOR -----------------------------------
    @Test void ballonLibrePuisPorte() {
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("d4"));
        Ballon b = new Ballon(p, Position.of("e5"));
        assertTrue(b.estLibre());
        assertTrue(b.porteuse().isEmpty());
        assertEquals(Position.of("e5"), b.position());
        assertTrue(p.caseA(Position.of("e5")).porteBallonLibre());

        b.prendrePossession(tour);
        assertFalse(b.estLibre());
        assertSame(tour, b.porteuse().orElseThrow());
        assertEquals(Position.of("d4"), b.position());
        assertFalse(p.caseA(Position.of("e5")).porteBallonLibre(), "un seul ballon, un seul endroit");
    }
    @Test void ballonPosePuisRelache() {
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("d4"));
        Ballon b = new Ballon(p, Position.of("d4"));
        b.prendrePossession(tour);
        b.poserEn(Position.of("e5"));
        assertTrue(b.estLibre());
        assertTrue(b.porteuse().isEmpty());
        assertTrue(p.caseA(Position.of("e5")).porteBallonLibre());
        assertFalse(p.caseA(Position.of("d4")).porteBallonLibre());
    }
    @Test void leBallonPorteSuitLaPieceQuiSeDeplace() {   // dribble, réponse client n° 4
        Plateau p = new Plateau();
        Piece tour = new Piece(TypePiece.TOUR);
        p.placer(tour, Position.of("d4"));
        Ballon b = new Ballon(p, Position.of("d4"));
        b.prendrePossession(tour);
        p.placer(tour, Position.of("d7"));
        assertEquals(Position.of("d7"), b.position());
    }

    // ---- Q9 : motifs et trajectoire --------------------------------------
    @Test void tourEnLigne() {
        assertTrue(Motif.aligne(TypePiece.TOUR, Position.of("b2"), Position.of("b6")));
        assertTrue(Motif.aligne(TypePiece.TOUR, Position.of("b2"), Position.of("g2")));
        assertFalse(Motif.aligne(TypePiece.TOUR, Position.of("b2"), Position.of("c3")));
    }
    @Test void fouEnDiagonaleDameLesDeux() {
        assertTrue(Motif.aligne(TypePiece.FOU, Position.of("c1"), Position.of("f4")));
        assertFalse(Motif.aligne(TypePiece.FOU, Position.of("c1"), Position.of("c4")));
        assertTrue(Motif.aligne(TypePiece.DAME, Position.of("d4"), Position.of("d8")));
        assertTrue(Motif.aligne(TypePiece.DAME, Position.of("d4"), Position.of("h8")));
        assertFalse(Motif.aligne(TypePiece.DAME, Position.of("d4"), Position.of("e6")));
    }
    @Test void cavalierEnL() {
        assertTrue(Motif.aligne(TypePiece.CAVALIER, Position.of("b1"), Position.of("c3")));
        assertTrue(Motif.aligne(TypePiece.CAVALIER, Position.of("a1"), Position.of("c2")));
        assertFalse(Motif.aligne(TypePiece.CAVALIER, Position.of("b1"), Position.of("b3")));
        assertFalse(Motif.glisse(TypePiece.CAVALIER));
        assertTrue(Motif.glisse(TypePiece.TOUR));
    }
    @Test void surPlaceJamaisAligne() {
        for (TypePiece t : TypePiece.values()) {
            assertFalse(Motif.aligne(t, Position.of("b2"), Position.of("b2")), t.toString());
        }
    }
    @Test void trajectoireObstrueeOuLibre() {
        Plateau p = new Plateau();
        assertTrue(p.trajectoireLibre(Position.of("b2"), Position.of("b6")));
        assertTrue(p.trajectoireLibre(Position.of("b2"), Position.of("b3")), "cases adjacentes : rien entre les deux");
        p.placer(new Piece(TypePiece.FOU), Position.of("b4"));
        assertFalse(p.trajectoireLibre(Position.of("b2"), Position.of("b6")));
        assertFalse(p.trajectoireLibre(Position.of("b6"), Position.of("b2")));
        assertTrue(p.trajectoireLibre(Position.of("a1"), Position.of("h8")));
        p.placer(new Piece(TypePiece.TOUR), Position.of("d4"));
        assertFalse(p.trajectoireLibre(Position.of("a1"), Position.of("h8")));
    }
}
