package velo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/** Suite de l'encadrant·e, TP2 Q1-Q2 : association bidirectionnelle Velo - Guidon. */
public class VeloGuidonTest {
	Guidon guidon;
	Velo velo;

	@BeforeEach
	void setUp() {
		guidon = new Guidon();
		velo = new Velo(new Selle());
	}

	@Test
	void getSetGuidon() {
		velo.setGuidon(guidon);

		assertSame(guidon, velo.getGuidon());
		assertSame(velo, guidon.getVelo());
	}

	@Test
	void setVeloDepuisLeGuidon() {
		guidon.setVelo(velo);

		assertSame(guidon, velo.getGuidon());
		assertSame(velo, guidon.getVelo());
	}

	@Test
	void removeGuidon() {
		velo.setGuidon(guidon);
		velo.setGuidon(null);

		assertNull(guidon.getVelo());
		assertNull(velo.getGuidon());
	}

	@Test
	void removeDepuisLeGuidon() {
		velo.setGuidon(guidon);
		guidon.setVelo(null);

		assertNull(guidon.getVelo());
		assertNull(velo.getGuidon());
	}

	@Test
	void setGuidonNullSansGuidon() {
		velo.setGuidon(null);

		assertNull(guidon.getVelo());
		assertNull(velo.getGuidon());
	}

	@Test
	void remplacerLeGuidon() {
		Guidon guidon2 = new Guidon();
		velo.setGuidon(guidon);
		velo.setGuidon(guidon2);

		assertSame(guidon2, velo.getGuidon());
		assertSame(velo, guidon2.getVelo());
		assertNull(guidon.getVelo());
	}

	@Test
	void unGuidonChangeDeVelo() {
		Velo velo2 = new Velo(new Selle());
		velo.setGuidon(guidon);
		velo2.setGuidon(guidon);

		assertSame(velo2, guidon.getVelo());
		assertSame(guidon, velo2.getGuidon());
		assertNull(velo.getGuidon());
	}
}
