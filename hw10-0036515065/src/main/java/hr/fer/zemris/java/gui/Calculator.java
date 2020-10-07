package hr.fer.zemris.java.gui;

import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;
import hr.fer.zemris.java.gui.calc.CalcOperations;
import hr.fer.zemris.java.gui.calc.buttons.CalcButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleCalcButton;
import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Program which demonstrates a simple Calculator which imitates the Calculator
 * in Windows XP systems.
 * 
 * @author Jura Milić
 *
 */
public class Calculator extends JFrame {
	private static final long serialVersionUID = 1L;
	/**
	 * The Container containing the Calculator.
	 */
	Container cp;
	/**
	 * The CalcModel the Calculator is using.
	 */
	CalcModel model;
	/**
	 * List of buttons which invert their functions when "Inv" button is pressed.
	 */
	List<InvertibleCalcButton> invButtons;
	/**
	 * Stack of Double values.
	 */
	Stack<Double> stack;

	/**
	 * Constructs a Calculator
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	/**
	 * Initializes the Calculator GUI.
	 */
	private void initGUI() {
		cp = getContentPane();
		model = new CalcModelImpl();
		invButtons = new ArrayList<InvertibleCalcButton>();
		stack = new Stack<Double>();

		cp.setLayout(new CalcLayout(5));

		JLabel screen = new JLabel("", JLabel.RIGHT);
		screen.setBackground(Color.ORANGE);
		screen.setOpaque(true);
		screen.setFont(screen.getFont().deriveFont(30f));
		cp.add(screen, new RCPosition(1, 1));

		model.addCalcValueListener(m -> screen.setText(model.toString()));

		cp.add(new CalcButton("=", e -> {
			DoubleBinaryOperator operator = model.getPendingBinaryOperation();
			double firstOperand = model.getActiveOperand();
			double secondOperand = model.getValue();

			model.setValue(operator.applyAsDouble(firstOperand, secondOperand));
		}), new RCPosition(1, 6));

		cp.add(new CalcButton("reset", e -> model.clearAll()), new RCPosition(2, 7));
		cp.add(new CalcButton("clr", e -> model.clear()), new RCPosition(1, 7));
		cp.add(new CalcButton("push", e -> stack.push(model.getValue())), new RCPosition(3, 7));
		cp.add(new CalcButton("pop", e -> {
			try {
				model.setValue(stack.pop());
			} catch (EmptyStackException ex) {
				screen.setText("Stack empty! Reset calculator");
			}
		}), new RCPosition(4, 7));
		cp.add(new CalcButton("+/-", e -> model.swapSign()), new RCPosition(5, 4));
		cp.add(new CalcButton(".", e -> model.insertDecimalPoint()), new RCPosition(5, 5));

		cp.add(new CalcButton("1/x", e -> model.setValue(1 / model.getValue())), new RCPosition(2, 1));
		
		addInvertibleButton(new InvertibleCalcButton("sin", e -> model.setValue(Math.sin(model.getValue())), "arcSin",
				e -> model.setValue(Math.asin(model.getValue()))), new RCPosition(2, 2));

		addInvertibleButton(new InvertibleCalcButton("log", e -> model.setValue(Math.log10((model.getValue()))), "10^",
				e -> model.setValue(Math.pow(10, model.getValue()))), new RCPosition(3, 1));

		addInvertibleButton(new InvertibleCalcButton("cos", e -> model.setValue(Math.cos(model.getValue())), "arcCos",
				e -> model.setValue(Math.acos(model.getValue()))), new RCPosition(3, 2));

		addInvertibleButton(new InvertibleCalcButton("ln", e -> model.setValue(Math.log((model.getValue()))), "e^",
				e -> model.setValue(Math.pow(Math.E, model.getValue()))), new RCPosition(4, 1));

		addInvertibleButton(new InvertibleCalcButton("tan", e -> model.setValue(Math.tan(model.getValue())), "arcTan",
				e -> model.setValue(Math.atan(model.getValue()))), new RCPosition(4, 2));

		addInvertibleButton(new InvertibleCalcButton("ctg", e -> model.setValue(1 / Math.tan(model.getValue())),
				"arcCtg", e -> model.setValue(1 / Math.atan(model.getValue()))), new RCPosition(5, 2));

		addInvertibleButton(new InvertibleCalcButton("x^n", e -> model.setPendingBinaryOperation(CalcOperations.POWER),
				"x^(1/n)", e -> model.setPendingBinaryOperation(CalcOperations.ROOT)), new RCPosition(5, 1));

		cp.add(new CalcButton("+", e -> model.setPendingBinaryOperation(CalcOperations.SUM)), new RCPosition(5, 6));
		cp.add(new CalcButton("/", e -> model.setPendingBinaryOperation(CalcOperations.DIVISION)),
				new RCPosition(2, 6));
		cp.add(new CalcButton("*", e -> model.setPendingBinaryOperation(CalcOperations.MULTIPLY)),
				new RCPosition(3, 6));
		cp.add(new CalcButton("-", e -> model.setPendingBinaryOperation(CalcOperations.DIFFERENCE)),
				new RCPosition(4, 6));

		addDigit(0, new RCPosition(5, 3));
		for (int i = 1; i < 10; i++) {
			int row = 4 - (i - 1) / 3;
			int column = (i - 1) % 3 + 3;
			addDigit(i, new RCPosition(row, column));
		}

		JCheckBox inv = new JCheckBox("Inv");
		inv.setFont(inv.getFont().deriveFont(20f));
		cp.add(inv, new RCPosition(5, 7));

		inv.addActionListener(e -> {
			for (InvertibleCalcButton button : invButtons) {
				button.switchOperation();
			}
		});
	}

	/**
	 * Adds the given invertible button to the list and parent Container with the
	 * given RCPosition constraint.
	 * 
	 * @param button given button
	 * @param pos    given constraint
	 */
	private void addInvertibleButton(InvertibleCalcButton button, RCPosition pos) {
		cp.add(button, pos);
		invButtons.add(button);
	}

	/**
	 * Adds a digit button to the Calculator which adds the given digit to the
	 * calculator value when the digit button is pressed. The digit button will be
	 * added to the parent Container with given RCPosition constraint.
	 * 
	 * @param digit given digit
	 * @param pos   given constraint
	 */
	private void addDigit(int digit, RCPosition pos) {
		cp.add(new CalcButton(Integer.toString(digit), e -> model.insertDigit(digit)), pos);
	}

	/**
	 * Start of program
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new Calculator().setVisible(true);
		});
	}
}
