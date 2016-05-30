import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author Martin Hobby
 *
 */
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires <pre>
     * [exp is a subtree of a well-formed XML arithmetic expression]  and
     *  [the label of the root of exp is not "expression"]
     * </pre>
     * @ensures evaluate = [the value of the expression]
     */
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        /**
         * An Natural Number value, called 'result', is created which will
         * become the result of the xml tree exp's arithmetic value, and then
         * returned
         */
        NaturalNumber result = new NaturalNumber2(0);

        /**
         * If the xml tree has tag "number", it will parse the value to an int
         */
        if (exp.label().equals("number")) {
            int value = Integer.parseInt(exp.attributeValue("value"));
            result = new NaturalNumber2(value);
        }

        /**
         * An xml tree with a value other than "number", will evaluate the
         * 'left' and 'right' child values in relation to the arithmetic
         * expression found in the label value
         */
        else {
            /**
             * two Natural Number variables are created with 'left' being the
             * first child of the xml tree, and 'right' being the second child
             */
            NaturalNumber left = new NaturalNumber2(evaluate(exp.child(0)));
            NaturalNumber right = new NaturalNumber2(evaluate(exp.child(1)));

            /**
             * 'left' and 'right' are evaluated based on their associated
             * arithmetic expression and then applied to the result variable.
             * Due to Natural Numbers having to be > 0, if the expression
             * requires subtraction, a test is run to ensure that 'right' is not
             * greater than 'left', since if it was the result would be < 0.
             * 'right' is also tested if it is 0 if the expression calls to be
             * divided, and an error message is printed if it is, since a number
             * cannot be divided by zero.
             */
            if (exp.label().equals("plus")) {
                left.add(right);
                result = left;
            } else if (exp.label().equals("minus")) {
                if (left.compareTo(right) < 1) {
                    fatalErrorToConsole("Error. Natural Number cannot be less than 0.");
                }
                left.subtract(right);
                result = left;
            } else if (exp.label().equals("times")) {
                left.multiply(right);
                result = left;
            } else if (exp.label().equals("divide")) {
                if (right.isZero()) {
                    fatalErrorToConsole("Error. Cannot divide by zero.");
                }
                assert !right.isZero() : "Error. Cannot divide by zero.";
                left.divide(right);
                result = left;
            }
        }

        return result;
    }

    public static void fatalErrorToConsole(String msg) {
        assert msg != null : "Violation of: msg is not null";
        throw new RuntimeException(msg);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}