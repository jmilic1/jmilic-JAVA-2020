package hr.fer.zemris.math;

import hr.fer.zemris.math.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector2DTest {

	@Test
	void translateTest() {
		Vector2D vector = new Vector2D(2, 3);
		Vector2D vector2 = new Vector2D(3, 4);
		vector.translate(vector2);

		assertEquals(5, vector.getX());
		assertEquals(7, vector.getY());

		Vector2D vector3 = vector2.translated(vector);
		assertEquals(8, vector3.getX());
		assertEquals(11, vector3.getY());
	}

	@Test
	void rotateTest() {
		Vector2D vector = new Vector2D(1, 0);
		vector.rotate(Math.PI / 2);

		assertTrue(vector.getX() < 1E-10);
		assertEquals(1, vector.getY());

		vector.rotate(-Math.PI / 2);

		double delta = 1E-3;

		assertEquals(1, vector.getX(), delta);
		assertEquals(0, vector.getY(), delta);

		vector.rotate(Math.PI * 3 / 4);
		assertEquals(-Math.sqrt(2) / 2, vector.getX(), delta);
		assertEquals(Math.sqrt(2) / 2, vector.getY(), delta);

		Vector2D vector2 = vector.rotated(-Math.PI);
		assertEquals(Math.sqrt(2) / 2, vector2.getX(), delta);
		assertEquals(-Math.sqrt(2) / 2, vector2.getY(), delta);

		Vector2D vector3 = vector2.rotated(-Math.PI);
		assertEquals(-Math.sqrt(2) / 2, vector3.getX(), delta);
		assertEquals(Math.sqrt(2) / 2, vector3.getY(), delta);
	}

	@Test
	void scaleTest() {
		Vector2D vector = new Vector2D(2, 2);
		vector.scale(0.5);

		assertEquals(1, vector.getX());
		assertEquals(1, vector.getY());

		vector.scale(-5);
		assertEquals(-5, vector.getX());
		assertEquals(-5, vector.getY());

		Vector2D vector2 = vector.scaled(10);
		assertEquals(-50, vector2.getX());
		assertEquals(-50, vector2.getY());
	}

	@Test
	void copyTest() {
		Vector2D vector = new Vector2D(2, 2);
		Vector2D vector2 = vector.copy();

		assertTrue(vector.getX() == vector2.getX());
		assertTrue(vector.getY() == vector2.getY());
	}

	@Test
	void constructorGetTest() {
		Vector2D vector = new Vector2D(5, 3);
		assertEquals(5, vector.getX());
		assertEquals(3, vector.getY());
	}

}
