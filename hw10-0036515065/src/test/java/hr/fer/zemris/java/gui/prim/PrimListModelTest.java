package hr.fer.zemris.java.gui.prim;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrimListModelTest {

	@Test
	void constructorTest() {
		PrimListModel model = new PrimListModel();
		assertEquals(1, model.getSize());
		assertEquals(1, model.getElementAt(0));
	}
	
	@Test
	void fifthPrimeTest() {
		PrimListModel model = new PrimListModel();
		model.next();
		model.next();
		model.next();
		model.next();
		assertEquals(5, model.getSize());
		assertEquals(7, model.getElementAt(4));
	}
}
