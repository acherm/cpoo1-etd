package chessball;

import chessball.moteur.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Modifier;

import static chessball.moteur.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de l'encadrant·e du TP3 : le moteur statique (règles officielles,
 * plateau 7×6). Compile contre les signatures du sujet ; à copier dans
 * src/test/java2/chessball/.
 */
@DisplayName("TP3 — du modèle au code, moteur statique")
class SuiteProfTP3Test {

    // ---- Q3 : Position -----------------------------------------------------
    @Test void positionValide() { assertEquals(new Position(3, 3), Position.of("d4")); }
    @Test void positionCoins() {
        assertEquals("a1", Position.of("a1").toString());
        assertEquals("g6", Position.of("g6").toString());
    }
    @Test void positionHorsPlateau() {
        assertThrows(IllegalArgumentException.class, () -> new Position(7, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 6));
        assertThrows(IllegalArgumentException.class, () -> Position.of("h1"));
        assertThrows(IllegalArgumentException.class, () -> Position.of("a7"));
    }
    @Test void voisinesEtBords() {
        assertEquals(Position.of("d5"), Position.of("d4").voisine(NORD).orElseThrow());
        assertEquals(Position.of("c3"), Position.of("d4").voisine(SUD_OUEST).orElseThrow());
        assertTrue(Position.of("a1").voisine(OUEST).isEmpty());
        assertTrue(Position.of("g6").voisine(NORD).isEmpty());
    }
    @Test void zonesDeToucheEtLignesDeBut() {
        assertTrue(Position.of("a3").estZoneDeTouche());
        assertTrue(Position.of("g2").estZoneDeTouche());
        assertFalse(Position.of("a1").estZoneDeTouche(), "les coins sont des lignes de but, pas des zones de touche");
        assertFalse(Position.of("b3").estZoneDeTouche());
        assertTrue(Position.of("c1").estLigneDeBut());
        assertTrue(Position.of("a6").estLigneDeBut());
        assertFalse(Position.of("d4").estLigneDeBut());
    }
    @Test void egaliteStructurelle() {
        assertEquals(Position.of("d4"), new Position(3, 3));
        assertEquals(Position.of("d4").hashCode(), new Position(3, 3).hashCode());
        assertNotSame(Position.of("d4"), new Position(3, 3));
    }
    @Test void couleurs() {
        assertEquals(0, Couleur.BLEUS.rangeeDeBut());
        assertEquals(5, Couleur.ROUGES.rangeeDeBut());
        assertEquals(Couleur.BLEUS, Couleur.ROUGES.adverse());
    }

    // ---- Q4 : Coup ---------------------------------------------------------
    @Test void coupsEgauxParValeur() {
        assertEquals(new Poussee(Position.of("c5"), SUD_EST), new Poussee(Position.of("c5"), SUD_EST));
        assertNotEquals(new Poussee(Position.of("c5"), SUD_EST), new Deplacement(Position.of("c5"), SUD_EST));
    }
    @Test void unSwitchSurCoupDoitAvoirUnDefault() {
        Coup c = new Saut(Position.of("c5"), SUD_EST);
        String nom = switch (c) {
            case Deplacement d -> "deplacement";
            case Poussee p     -> "poussee";
            case Tacle t       -> "tacle";
            case Saut s        -> "saut";
            default -> throw new IllegalStateException("type de coup inconnu");
        };
        assertEquals("saut", nom);
        assertEquals(Position.of("c5"), c.origine());
        assertEquals(SUD_EST, c.direction());
    }

    // ---- Q5/Q6 : Equipe et Piece ---------------------------------------------
    @Test void ajouterEtablitLesDeuxSens() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        Piece a = new Piece(TypePiece.ATTAQUANT);
        assertNull(a.equipe());
        bleus.ajouter(a);
        assertSame(bleus, a.equipe());
        assertEquals(Couleur.BLEUS, a.couleur());
        assertTrue(bleus.pieces().contains(a));
        assertEquals(TypePiece.ATTAQUANT, a.type());
    }
    @Test void ajoutEnDoubleEstIdempotent() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        Piece d = new Piece(TypePiece.DEFENSEUR);
        bleus.ajouter(d); bleus.ajouter(d);
        assertEquals(1, bleus.pieces().size());
    }
    @Test void sixiemePieceRefusee() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        for (int i = 0; i < Equipe.EFFECTIF; i++) bleus.ajouter(new Piece(TypePiece.DEFENSEUR));
        assertEquals(5, Equipe.EFFECTIF);
        assertThrows(IllegalStateException.class, () -> bleus.ajouter(new Piece(TypePiece.ATTAQUANT)));
    }
    @Test void pieceDejaAligneeAilleursRefusee() {
        Equipe bleus = new Equipe(Couleur.BLEUS), rouges = new Equipe(Couleur.ROUGES);
        Piece p = new Piece(TypePiece.ATTAQUANT);
        bleus.ajouter(p);
        assertThrows(IllegalStateException.class, () -> rouges.ajouter(p));
    }
    @Test void laVueExposeeEstNonModifiable() {
        Equipe bleus = new Equipe(Couleur.BLEUS);
        bleus.ajouter(new Piece(TypePiece.ATTAQUANT));
        assertThrows(UnsupportedOperationException.class, () -> bleus.pieces().clear());
    }

    // ---- Q7 : Plateau et Case, composition -----------------------------------
    @Test void plateauCreeSes42Cases() {
        Plateau p = new Plateau();
        assertNotNull(p.caseA(Position.of("a1")));
        assertNotNull(p.caseA(Position.of("g6")));
        assertEquals(Position.of("d4"), p.caseA(Position.of("d4")).position());
        assertTrue(p.pieceEn(Position.of("d4")).isEmpty());
        assertTrue(p.estLibre(Position.of("d4")));
    }
    @Test void seulLePlateauCreeDesCases() throws Exception {
        int modificateurs = Case.class.getDeclaredConstructor(Position.class).getModifiers();
        assertFalse(Modifier.isPublic(modificateurs), "le constructeur de Case ne doit pas être public");
    }
    @Test void placerPuisRelire() {
        Plateau p = new Plateau();
        Piece a = new Piece(TypePiece.ATTAQUANT);
        p.placer(a, Position.of("c5"));
        assertSame(a, p.pieceEn(Position.of("c5")).orElseThrow());
        assertEquals(Position.of("c5"), p.positionDe(a).orElseThrow());
        assertTrue(p.caseA(Position.of("c5")).occupee());
        assertFalse(p.estLibre(Position.of("c5")));
    }
    @Test void caseOccupeeRefusee() {
        Plateau p = new Plateau();
        p.placer(new Piece(TypePiece.ATTAQUANT), Position.of("c5"));
        assertThrows(IllegalStateException.class, () -> p.placer(new Piece(TypePiece.DEFENSEUR), Position.of("c5")));
    }
    @Test void deplacerLibereLAncienneCaseEtRetirerAussi() {
        Plateau p = new Plateau();
        Piece a = new Piece(TypePiece.ATTAQUANT);
        p.placer(a, Position.of("c5"));
        p.placer(a, Position.of("c4"));
        assertTrue(p.estLibre(Position.of("c5")));
        assertEquals(Position.of("c4"), p.positionDe(a).orElseThrow());
        p.retirer(a);
        assertTrue(p.positionDe(a).isEmpty());
        assertTrue(p.estLibre(Position.of("c4")));
    }

    // ---- Q8 : le ballon --------------------------------------------------------
    @Test void leBallonOccupeSaCaseSansEtreUnePiece() {
        Plateau p = new Plateau();
        Ballon b = new Ballon(p, Position.of("d4"));
        assertEquals(Position.of("d4"), b.position());
        assertTrue(p.caseA(Position.of("d4")).porteBallon());
        assertFalse(p.caseA(Position.of("d4")).occupee());
        assertFalse(p.estLibre(Position.of("d4")), "une case qui porte le ballon n'est pas libre");
        assertThrows(IllegalStateException.class, () -> p.placer(new Piece(TypePiece.ATTAQUANT), Position.of("d4")));
    }
    @Test void leBallonSeDeplaceEtLibereSaCase() {
        Plateau p = new Plateau();
        Ballon b = new Ballon(p, Position.of("d4"));
        b.deplacerVers(Position.of("e3"));
        assertEquals(Position.of("e3"), b.position());
        assertTrue(p.estLibre(Position.of("d4")));
        assertTrue(p.caseA(Position.of("e3")).porteBallon());
    }
    @Test void leBallonRefuseLesZonesDeToucheEtLesCasesNonLibres() {
        Plateau p = new Plateau();
        p.placer(new Piece(TypePiece.DEFENSEUR), Position.of("c3"));
        Ballon b = new Ballon(p, Position.of("b3"));
        assertThrows(IllegalArgumentException.class, () -> b.deplacerVers(Position.of("a3")));
        assertThrows(IllegalStateException.class, () -> b.deplacerVers(Position.of("c3")));
        assertThrows(IllegalArgumentException.class, () -> new Ballon(p, Position.of("g4")));
        assertEquals(Position.of("b3"), b.position(), "un déplacement refusé ne change rien");
    }
}
