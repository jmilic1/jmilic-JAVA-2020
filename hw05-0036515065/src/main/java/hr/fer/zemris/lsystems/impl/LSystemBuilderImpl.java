package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.NoSuchElementException;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;
import hr.fer.zemris.math.Vector2D;

/**
 * Implementation of LSystemBuilder which interprets a Lindermayer System from a
 * given input. Inputs can be a String of text or a series of LSystemBuilder's
 * method calls. If LSystemBuilder's variables were not set, default values will
 * be used. There are no default values for commands and productions.
 * 
 * @author Jura Milić
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	/**
	 * Value used for getting the Radians value from Degree
	 */
	private static final double DEGREE_TO_RAD = Math.PI / 180;
	/**
	 * Default color of drawing.
	 */
	private static final Color DEFAULT_COLOR = Color.BLACK;
	/**
	 * A Dictionary that contains which Characters should be produced into which
	 * String when going through a new level of system.
	 */
	private Dictionary<Character, String> productions;
	/**
	 * A Dictionary that contains which Characters represent which Commands.
	 */
	private Dictionary<Character, Command> commands;
	/**
	 * Basic length of drawn lines.
	 */
	private double unitLength;
	/**
	 * How much unitLength should be scaled.
	 */
	private double unitLengthDegreeScaler;
	/**
	 * Vector which points to the starting position of System.
	 */
	private Vector2D origin;
	/**
	 * Angle in degrees which defines the initial direction of the Turtle.
	 */
	private double angle;
	/**
	 * Starting input for System.
	 */
	private String axiom;

	/**
	 * Constructs a LSystemBuilderImpl with default values.
	 */
	public LSystemBuilderImpl() {
		unitLength = 0.1;
		unitLengthDegreeScaler = 1;
		origin = new Vector2D(0, 0);
		angle = 0;
		axiom = "";
		productions = new Dictionary<>();
		commands = new Dictionary<>();
	}

	/**
	 * Makes an LSystem class which implements a way to interpret
	 * LSystemBuilderImpl's variables into a Lindermayer System.
	 * 
	 * @return the newly made LSystem class
	 */
	@Override
	public LSystem build() {
		return new LSystem() {
			/**
			 * Context for different TurtleStates which are used.
			 */
			private Context context;

			/**
			 * Draws a Lindermayer System interpreted from LSystemBuilderImpl's inputs.
			 */
			@Override
			public void draw(int arg0, Painter arg1) {
				context = new Context();

				context.pushState(new TurtleState(origin.copy(),
						new Vector2D(Math.cos(angle * DEGREE_TO_RAD), Math.sin(angle * DEGREE_TO_RAD)), DEFAULT_COLOR,
						unitLength * (Math.pow(unitLengthDegreeScaler, arg0))));

				String generated = generate(arg0);

				for (char symbol : generated.toCharArray()) {
					try {
						commands.get(symbol).execute(context, arg1);
					} catch (NoSuchElementException ex) {
					}
				}
			}

			/**
			 * Generates a String of characters where each character represents a command.
			 * 
			 * @param level number of times axiom will be transformed
			 * @return generated String
			 */
			@Override
			public String generate(int level) {
				String generated = axiom;

				for (int i = 0; i < level; i++) {
					StringBuilder sb = new StringBuilder();

					for (char symbol : generated.toCharArray()) {
						try {
							sb.append(productions.get(symbol));
						} catch (NoSuchElementException ex) {
							sb.append(symbol);
						}
					}
					generated = sb.toString();
				}
				return generated;
			}
		};
	}

	/**
	 * Reads given String input and interprets it into a LSystemBuilder.
	 * 
	 * @param arg0 given input
	 * @return generated LSystemBuilder
	 */
	@Override
	public LSystemBuilder configureFromText(String[] arg0) {
		for (String string : arg0) {
			String[] line = string.split("\\s+");

			switch (line[0]) {
			case ("origin"):
				setOrigin(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
				break;
			case ("angle"):
				setAngle(Double.parseDouble(line[1]));
				break;
			case ("unitLength"):
				setUnitLength(Double.parseDouble(line[1]));
				break;
			case ("unitLengthDegreeScaler"):
				readUnitLengthDegreeScaler(line);
				break;
			case ("command"):
				if (line.length == 3)
					registerCommand(line[1].charAt(0), line[2]);
				else
					registerCommand(line[1].charAt(0), line[2] + " " + line[3]);
				break;
			case ("axiom"):
				setAxiom(line[1]);
				break;
			case ("production"):
				registerProduction(line[1].charAt(0), line[2]);
				break;
			}
		}
		return this;
	}

	/**
	 * Reads the unit length degree scaler from given array of Strings.
	 * 
	 * @param line given array of Strings.
	 */
	private void readUnitLengthDegreeScaler(String[] line) {
		if (line.length == 2) {
			setUnitLengthDegreeScaler(Double.parseDouble(line[1]));
		} else {
			if (line.length == 4) {
				setUnitLengthDegreeScaler(Double.parseDouble(line[1]) / Double.parseDouble(line[3]));
			} else {
				double number;
				try {
					number = Double.parseDouble(line[1]);
					line[2] = line[2].substring(1);

					setUnitLengthDegreeScaler(number / Double.parseDouble(line[2]));
				} catch (NumberFormatException ex) {
					line[1] = line[1].substring(0, line[1].length() - 2);

					setUnitLengthDegreeScaler(Double.parseDouble(line[1]) / Double.parseDouble(line[2]));
				}
			}
		}
	}

	/**
	 * Reads given String to determine the command and its parameter. Command is
	 * paired with given character.
	 * 
	 * @param arg0 given char
	 * @param arg1 given String
	 * @return LSystemBuilder with the new char-command pair in it
	 */
	@Override
	public LSystemBuilder registerCommand(char arg0, String arg1) {
		String[] input = arg1.split("\\s+");

		if (input.length == 1) {
			switch (input[0]) {
			case ("push"):
				commands.put(arg0, new PushCommand());
				break;
			case ("pop"):
				commands.put(arg0, new PopCommand());
				break;
			default:
				throw new IllegalArgumentException();
			}
		} else {
			if (input.length == 2) {
				switch (input[0]) {
				case ("draw"):
					commands.put(arg0, new DrawCommand(Double.parseDouble(input[1])));
					break;
				case ("skip"):
					commands.put(arg0, new SkipCommand(Double.parseDouble(input[1])));
					break;
				case ("scale"):
					commands.put(arg0, new ScaleCommand(Double.parseDouble(input[1])));
					break;
				case ("rotate"):
					commands.put(arg0, new RotateCommand(Double.parseDouble(input[1])));
					break;
				case ("color"):
					commands.put(arg0, new ColorCommand(Color.decode("#" + input[1])));
				}
			}
		}
		return this;
	}

	/**
	 * Writes a production into Dictionary
	 * 
	 * @param arg0 char which will be translated
	 * @param arg1 String which will replace char
	 */
	@Override
	public LSystemBuilder registerProduction(char arg0, String arg1) {
		productions.put(arg0, arg1);
		return this;
	}

	/**
	 * @param arg0 angle
	 */
	@Override
	public LSystemBuilder setAngle(double arg0) {
		angle = arg0;
		return this;
	}

	/**
	 * @param arg0 axiom
	 */
	@Override
	public LSystemBuilder setAxiom(String arg0) {
		axiom = arg0;
		return this;
	}

	/**
	 * Sets Origin vector
	 * 
	 * @param arg0 xValue of origin
	 * @param arg1 yValue of origin
	 */
	@Override
	public LSystemBuilder setOrigin(double arg0, double arg1) {
		origin = new Vector2D(arg0, arg1);
		return this;
	}

	/**
	 * @param arg0 unitLength
	 */
	@Override
	public LSystemBuilder setUnitLength(double arg0) {
		unitLength = arg0;
		return this;
	}

	/**
	 * @param arg0 unitLengthDegreeScaler
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double arg0) {
		unitLengthDegreeScaler = arg0;
		return this;
	}
}
