package velo;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Suite de l'encadrant·e, TP2 Q5 : le bout Roue de la composition. */
public class RoueTest {
	Roue roue;
	Velo velo;

	@BeforeEach
	void setUp() {
		roue = new Roue();
		velo = new Velo(new Selle());
	}

	@Test
	void getVeloAuDepart() {
		assertNull(roue.getVelo());
	}

	@Test
	void setVelo() {
		roue.setVelo(velo);

		assertSame(velo, roue.getVelo());
		assertEquals(List.of(roue), velo.getRoues());
	}

	@Test
	void setVeloNull() {
		roue.setVelo(velo);
		roue.setVelo(null);

		assertNull(roue.getVelo());
		assertTrue(velo.getRoues().isEmpty());
	}

	@Test
	void setVeloDeuxFois() {
		roue.setVelo(velo);
		roue.setVelo(velo);

		assertEquals(List.of(roue), velo.getRoues());
	}
}
