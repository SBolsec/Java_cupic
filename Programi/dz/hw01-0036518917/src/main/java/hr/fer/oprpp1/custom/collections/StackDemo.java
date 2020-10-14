package hr.fer.oprpp1.custom.collections;

public class StackDemo {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("There must be only one argument!");
            System.exit(1);
        }

        String[] elements = args[0].split(" ");
        ObjectStack stack = new ObjectStack();

        try {
            for (String s : elements) {
                if (s.length() == 0 || Character.isWhitespace(s.charAt(0))) {
                    continue;
                } else if (isNumber(s)) {
                    stack.push(s);
                } else {
                    int first = Integer.parseInt((String) stack.pop());
                    int second = Integer.parseInt((String) stack.pop());
                    int result = 0;

                    switch (s.charAt(0)) {
                        case '+': result = second + first; break;
                        case '-': result = second - first; break;
                        case '/':
                            if (first == 0) throw new IllegalArgumentException("Dividing by zero!");
                            result = second / first; break;
                        case '*': result = second * first; break;
                        case '%': result = second % first; break;
                        default: throw new IllegalArgumentException("There is no such operation supported!");
                    }

                    stack.push(Integer.toString(result));
                }
            }

            if (stack.size() != 1) {
                System.err.println("There was a error!");
            } else {
                System.out.println("Expression evaluates to " + stack.pop() + ".");
            }
        } catch (IllegalArgumentException | EmptyStackException ex) {
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static boolean isNumber(String s) {
        try {
            int a = Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
