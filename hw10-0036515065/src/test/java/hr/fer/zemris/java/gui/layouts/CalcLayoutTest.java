package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.jupiter.api.Test;

class CalcLayoutTest {

	@Test
	void throwTest() {
		JPanel p = new JPanel(new CalcLayout(3));
		try {
			p.add(new JLabel("Trash"), new RCPosition(0, 1));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(-1, 1));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(2, 0));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(2, 8));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(2, 9));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(0, 9));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(0, 8));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(1, 2));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(1, 3));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(1, 4));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		try {
			p.add(new JLabel("Trash"), new RCPosition(1, 5));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}

		p.add(new JLabel("Not Thrash"), new RCPosition(5, 5));
		try {
			p.add(new JLabel("Thrash again"), new RCPosition(5, 5));
			fail("Should throw");
		} catch (CalcLayoutException ex) {
		}
	}

	@Test
	public void dimensionTest1() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(10, 30));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(20, 15));
		p.add(l1, new RCPosition(2, 2));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();

		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}

	@Test
	public void dimensionTest2() {
		JPanel p = new JPanel(new CalcLayout(2));
		JLabel l1 = new JLabel("");
		l1.setPreferredSize(new Dimension(108, 15));
		JLabel l2 = new JLabel("");
		l2.setPreferredSize(new Dimension(16, 30));
		p.add(l1, new RCPosition(1, 1));
		p.add(l2, new RCPosition(3, 3));
		Dimension dim = p.getPreferredSize();
		
		assertEquals(152, dim.width);
		assertEquals(158, dim.height);
	}

}
