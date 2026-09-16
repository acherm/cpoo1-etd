package velo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Suite de l'encadrant·e, TP2 Q3 : navigabilité Velo --> Selle, cardinalité 1. */
public class VeloSelleTest {
	@Test
	void selleObligatoire() {
		assertThrows(IllegalArgumentException.class, () -> new Velo(null));
	}

	@Test
	void getSelle() {
		Selle selle = new Selle();
		Velo velo = new Velo(selle);

		assertSame(selle, velo.getSelle());
	}
}
