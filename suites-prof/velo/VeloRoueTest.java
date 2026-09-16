package velo;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Suite de l'encadrant·e, TP2 Q4-Q5 : multiplicité, encapsulation, composition Velo *-- Roue. */
public class VeloRoueTest {
	Velo velo;

	@BeforeEach
	void setUp() {
		velo = new Velo(new Selle());
	}

	@Test
	void getRoues() {
		assertNotNull(velo.getRoues());
		assertTrue(velo.getRoues().isEmpty());
	}

	@Test
	void addNull() {
		velo.addRoue(null);
		assertTrue(velo.getRoues().isEmpty());
	}

	@Test
	void removeNull() {
		velo.removeRoue(null);
		assertTrue(velo.getRoues().isEmpty());
	}

	@Test
	void removeRoueAbsente() {
		velo.removeRoue(new Roue());
		assertTrue(velo.getRoues().isEmpty());
	}

	@Nested
	class WithRoues {
		Roue roue1;
		Roue roue2;

		@BeforeEach
		void setUp() {
			roue1 = new Roue();
			roue2 = new Roue();
			velo.addRoue(roue1);
			velo.addRoue(roue2);
		}

		@Test
		void addRoue() {
			assertEquals(List.of(roue1, roue2), velo.getRoues());
			assertSame(velo, roue1.getVelo());
			assertSame(velo, roue2.getVelo());
		}

		@Test
		void addRoueEnDouble() {
			velo.addRoue(roue1);
			assertEquals(List.of(roue1, roue2), velo.getRoues());
		}

		@Test
		void removeRoue1() {
			velo.removeRoue(roue1);
			assertEquals(List.of(roue2), velo.getRoues());
			assertNull(roue1.getVelo());
			assertSame(velo, roue2.getVelo());
		}

		@Test
		void removeRoue2() {
			velo.removeRoue(roue2);
			assertEquals(List.of(roue1), velo.getRoues());
			assertSame(velo, roue1.getVelo());
			assertNull(roue2.getVelo());
		}

		@Test
		void removeNull() {
			velo.removeRoue(null);
			assertEquals(List.of(roue1, roue2), velo.getRoues());
		}

		/** Q4c : vue non modifiable OU copie défensive, les deux protègent l'invariant. */
		@Test
		void encapsulation() {
			try {
				velo.getRoues().clear();
			} catch (UnsupportedOperationException attendueSiVueNonModifiable) {
				// vue non modifiable : le clear() est refusé
			}
			assertEquals(List.of(roue1, roue2), velo.getRoues());
		}

		@Test
		void uneRoueChangeDeVelo() {
			Velo velo2 = new Velo(new Selle());
			roue1.setVelo(velo2);

			assertEquals(List.of(roue2), velo.getRoues());
			assertEquals(List.of(roue1), velo2.getRoues());
			assertSame(velo2, roue1.getVelo());
		}
	}
}
