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
public final class XMLTreeIntExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeIntExpressionEvaluator() {
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
    private static int evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        /**
         * An int value, called 'result', is created which will become the
         * result of the xml tree exp's arithmetic value, and then returned
         */
        int result = 0;

        /**
         * If the xml tree has tag "number", it will parse the value to an int
         */
        if (exp.label().equals("number")) {
            result = Integer.parseInt(exp.attributeValue("value"));
        }

        /**
         * An xml tree with a value other than "number", will evaluate the
         * 'left' and 'right' child values in relation to the arithmetic
         * expression found in the label value
         */
        else {
            /**
             * two int variables are created with 'left' being the first child
             * of the xml tree, and 'right' being the second child
             */
            int left = evaluate(exp.child(0));
            int right = evaluate(exp.child(1));

            /**
             * 'left' and 'right' are evaluated based on their associated
             * arithmetic expression and then applied to the result variable
             */
            if (exp.label().equals("plus")) {
                result = left + right;
            } else if (exp.label().equals("minus")) {
                result = left - right;
            } else if (exp.label().equals("times")) {
                result = left * right;
            } else if (exp.label().equals("divide")) {
                result = left / right;
            }
        }

        return result;
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