package hr.fer.oprpp1.custom.collections.demo;

import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;

/**
 * Demo which tests the <code>ObjectStack</code> class
 * @author sbolsec
 *
 */
public class StackDemo {
    
	/** Array of the input split by whitespace **/
	private static String[] elements;
	/** Stack which will be used in parsing **/
	private static ObjectStack stack;
	
	/**
	 * Starting point of the program
	 * @param args takes the expression to be parsed
	 */
	public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("There must be only one argument!");
            System.exit(1);
        }

        elements = args[0].split(" ");
        stack = new ObjectStack();

        parse();
    }
    
	/**
	 * Method which does the actual parsing.
	 */
    private static void parse() {
    	try {
            for (String s : elements) {
                if (s.isBlank()) {
                    continue;
                } else if (isNumber(s)) {
                    stack.push(s);
                } else {
                    int first = Integer.parseInt((String) stack.pop());
                    int second = Integer.parseInt((String) stack.pop());
                    int result = 0;

                    switch (s) {
                        case "+": result = second + first; break;
                        case "-": result = second - first; break;
                        case "/":
                            if (first == 0) throw new IllegalArgumentException("Dividing by zero!");
                            result = second / first; break;
                        case "*": result = second * first; break;
                        case "%": result = second % first; break;
                        default: throw new IllegalArgumentException("Unsuported character: " + s.charAt(0));
                    }
                    stack.push(Integer.toString(result));
                }
            }

            if (stack.size() != 1) {
                System.out.println("The stack size at the end was different from 1, it was: " + stack.size() + ".");
            } else {
                System.out.println("Expression evaluates to " + stack.pop() + ".");
            }
        } catch (IllegalArgumentException | EmptyStackException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Checks whether the given string can be parsed as an integer
     * @param s string to be tested
     * @return true if the given string can be parsed as an integer, false otherwise
     */
    private static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
