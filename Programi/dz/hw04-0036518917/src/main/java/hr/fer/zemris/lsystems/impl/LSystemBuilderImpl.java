package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Scanner;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.ColorCommand;
import hr.fer.zemris.lsystems.impl.commands.DrawCommand;
import hr.fer.zemris.lsystems.impl.commands.PopCommand;
import hr.fer.zemris.lsystems.impl.commands.PushCommand;
import hr.fer.zemris.lsystems.impl.commands.RotateCommand;
import hr.fer.zemris.lsystems.impl.commands.ScaleCommand;
import hr.fer.zemris.lsystems.impl.commands.SkipCommand;

/**
 * This class holds all the configurations from which it can then create an instance
 * of <code>LSystem</code>
 * @author sbolsec
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/** Stores all the registered productions **/
	private Dictionary<Character, String> productions;
	/** Stores all the registered commands **/
	private Dictionary<Character, Command> commands;
	/** Length of the unit for drawing lines **/
	private double unitLength = 0.1;
	/** Scale which is used to scale the unit length through the different levels **/
	private double unitLengthScaler = 1;
	/** Starting position of the turtle **/
	private Vector2D origin = new Vector2D(0,0);
	/** Angle in which the turtle is facing **/
	private double angle = 0;
	/** Starting list of commands from which the fractals are generated **/
	private String axiom = "";
	
	/**
	 * Default constructor which creates the dictionaries
	 */
	public LSystemBuilderImpl() {
		productions = new Dictionary<>();
		commands = new Dictionary<>();
	}
	
	/**
	 * Creates a <code>LSystem</code> based on the configurations in this builder
	 * @return reference to <code>LSystem</code> created based on the configurations in this builder
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Configures all the properties of this builder from text
	 * @throws IllegalArgumentException if the input was invalid
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder configureFromText(String[] input) {
		
		for (String s : input) {
			if (s.isBlank()) continue;
			
			try (Scanner sc = new Scanner(s)) {
				String in = sc.next();
				switch(in) {
					case "origin":
						if (!sc.hasNextDouble()) throw new IllegalArgumentException("Double was expected after origin keyword!");
						double x = sc.nextDouble();
						if (!sc.hasNextDouble()) throw new IllegalArgumentException("Double was expected after origin keyword!");
						double y = sc.nextDouble();
						origin = new Vector2D(x, y);
						break;
					case "angle":
						if (!sc.hasNextDouble()) throw new IllegalArgumentException("Double was expected after angle keyword!");
						angle = degresToRadians(sc.nextDouble());
						break;
					case "unitLength":
						if (!sc.hasNextDouble()) throw new IllegalArgumentException("Double was expected after unitLength keyword!");
						unitLength = sc.nextDouble();
						break;
					case "unitLengthDegreeScaler":
						if (!sc.hasNext()) throw new IllegalArgumentException("Was expecting value after unitLengthDegreeScaler keyword!");
						String line = sc.nextLine();
						int indexOfDivisor = line.indexOf("/");
						if (indexOfDivisor == -1) {
							try {
								unitLengthScaler = Double.parseDouble(line);
								break;
							} catch (NumberFormatException ex) {
								throw new IllegalArgumentException("Was expecting a double!");
							}
						}
						try {
							double first = Double.parseDouble(line.substring(0, indexOfDivisor));
							double second = Double.parseDouble(line.substring(indexOfDivisor+1, line.length()));
							unitLengthScaler = first / second;
						} catch(Exception ex) {
							throw new IllegalArgumentException("Was expecting two values to be divided!");
						}
						break;
					case "command":
						if (!sc.hasNext()) throw new IllegalArgumentException("Character was expected after command keyword!");
						String key = sc.next();
						if (key.length() != 1) throw new IllegalArgumentException("Was expecting character, but got a string: " + key + ".");
						if (!sc.hasNext()) throw new IllegalArgumentException("There was no actual command!");
						String restOfLine = sc.nextLine();
						registerCommand(key.charAt(0), restOfLine.trim());
						break;
					case "axiom":
						if (!sc.hasNext()) throw new IllegalArgumentException("String was expected after axiom keyword!");
						axiom = sc.next();
						break;
					case "production":
						if (!sc.hasNext()) throw new IllegalArgumentException("Character was expected after axiom keyword!");
						key = sc.next();
						if (key.length() != 1) throw new IllegalArgumentException("Was expecting character, but got a string: " + key + ".");
						if (!sc.hasNext()) throw new IllegalArgumentException("There was no production!");
						restOfLine = sc.nextLine();
						productions.put(key.charAt(0), restOfLine.trim());
						break;
					default: throw new IllegalArgumentException("Input is not a valid keyword, it was: " + in + ".");
				}
			}
		}
		return this;
	}

	
	/**
	 * Parses the given command and registers it if it was valid.
	 * @throws IllegalArgumentException if the command is not valid
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder registerCommand(char key, String command) {
		if (commands.get(key) != null) throw new IllegalArgumentException("A command already exists for this symbol!");
		
		String[] input = command.trim().split("\s+");
		String type = input[0].toLowerCase();
		
		switch(type) {
			case "draw":
				double step = parseCommandArgument(input, "draw");
				commands.put(key, new DrawCommand(step));
				break;
			case "skip":
				double skip = parseCommandArgument(input, "skip");
				commands.put(key, new SkipCommand(skip));
				break;
			case "scale":
				double factor = parseCommandArgument(input, "scale");
				commands.put(key, new ScaleCommand(factor));
				break;
			case "rotate":
				double angle = degresToRadians(parseCommandArgument(input, "rotate"));
				commands.put(key, new RotateCommand(angle));
				break;
			case "push":
				if (input.length != 1) throw new IllegalArgumentException("Push command can only contain 'push'!");
				commands.put(key, new PushCommand());
				break;
			case "pop":
				if (input.length != 1) throw new IllegalArgumentException("Pop command can only contain 'pop'!");
				commands.put(key, new PopCommand());
				break;
			case "color":
				Color color = parseColor(input);
				commands.put(key, new ColorCommand(color));
				break;
			default:
				throw new IllegalArgumentException("Invaild command!");
		}

		return this;
	}

	/**
	 * Registers a production if its valid (left side of the production was not already registered)
	 * @param key character on the left side of the production
	 * @param value string on the right side of the production
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder registerProduction(char key, String value) {
		if (productions.get(key) != null) throw new IllegalArgumentException("A command already exists for this symbol!");
		
		productions.put(key, value);
		return this;
	}

	/**
	 * Sets the angle the turtle is facing in radians
	 * @param angle angle the turtle is facing
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;
		return this;
	}

	/**
	 * Sets the axiom for the fractal
	 * @param axiom axiom to be set
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;
		return this;
	}

	/**
	 * Sets the origin position of the turtle
	 * @param x x-coordinate of the position
	 * @param y y-coordinate of the position
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Sets the unit length
	 * @param unitLength unit length to be set
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Sets the unit length degree scaler
	 * @param unitLengthScaler unit length degree scaler to be set
	 * @return reference to this LSystemBuilder
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthScaler) {
		this.unitLengthScaler = unitLengthScaler;
		return this;
	}
	
	/**
	 * Parses the number part of the command.
	 * @param input array of the command split by whitespace
	 * @param commandName name of the command
	 * @return parsed double value of the number
	 * @throws IllegalArgumentException if the command was invalid
	 */
	private double parseCommandArgument(String[] input, String commandName) {
		if (input.length != 2) throw new IllegalArgumentException("Command must be in this form: '" + commandName + " s' where s is a number!");
		try {
			return Double.parseDouble(input[1]);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("There was no number, there was: " + input[1] + ".");
		}
	}

	/**
	 * Parses the color from the input command.
	 * @param input array of the command split by whitespace
	 * @return color to which to change the line to
	 * @throws IllegalArgumentException if the color is invalid
	 */
	private Color parseColor(String[] input) {
		if (input.length != 2) throw new IllegalArgumentException("Color must be in this form: 'color rrggbb'!");
		if (input[1].length() != 6) throw new IllegalArgumentException("Color value must be in form rrggbb, it was: " + input[1] + ".");
		
		int r = Integer.parseInt(input[1].substring(0, 2), 16);
		int g = Integer.parseInt(input[1].substring(2, 4), 16);
		int b = Integer.parseInt(input[1].substring(4, 6), 16);
		
		return new Color(r, g, b);
	}
	
	/**
	 * Converts the given angle from degrees to radians
	 * @param angle angle in degrees
	 * @return angle in radians
	 */
	private double degresToRadians(double angle) {
		while (angle > 360) {
			angle -= 360;
		}
		while (angle < 0) {
			angle += 360;
		}
		return angle * Math.PI / 180.0;
	}
	
	/**
	 * Implementation of <code>LSystem</code> interface which draws the fractals.
	 * @author sbolsec
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Draws the fractal from the generated list of commands for the given level.
		 * @param level number of times to repeat applying all the productions while generating list of commands
		 * @param painter painter which does the drawing
		 */
		@Override
		public void draw(int level, Painter painter) {
			Context ctx = new Context();
			double effectiveUnitLength = unitLength * Math.pow(unitLengthScaler, level);
			TurtleState initial = new TurtleState(origin.copy(), generateAngleVector(angle), Color.BLACK, effectiveUnitLength);
			ctx.pushState(initial);
			
			String generatedLine = generate(level);
			for (char c : generatedLine.toCharArray()) {
				Command command = commands.get(c);
				if (command == null) continue;
				command.execute(ctx, painter);
			}
			
		}

		/**
		 * Generates the list of commands for the given level by starting from the axiom
		 * and applying all the applicable productions and repeating it <code>level</code> times.
		 * @param level number of times to repeat applying all the productions
		 * @return list of commands for the given level
		 */
		@Override
		public String generate(int level) {
			String res = axiom;
			for (int i = 0; i < level; i++) {
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < res.length(); j++) {
					String production = productions.get(res.charAt(j));
					if (production == null) {
						sb.append(res.charAt(j));
						continue;
					}
					sb.append(production);
				}
				res = sb.toString();
			}
			return res;
		}
		
		/**
		 * Creates a vector from the given angle which is facing in the given angle.
		 * @param angle 
		 * @return vector 
		 */
		private Vector2D generateAngleVector(double angle) {
			return new Vector2D(Math.cos(angle), Math.sin(angle));
		}
	}
}
