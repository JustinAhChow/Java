import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program computes a double's square root using Newton Iteration.
 *
 * @author Martin Hobby
 *
 */
public final class Newton1 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Newton1() {
    }

    /*
     * Computes estimate of square root of x to within relative error 0.01%.
     *
     * @param x positive number to compute square root of
     *
     * @return estimate of square root
     */
    private static double sqrt(double x) {
        double r = x;
        final double e = 0.01;
        while (Math.abs(r * r - x) / x > e * e) {
            r = (r + x / r) / 2;
        }
        return r;
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

        /*
         * Boolean loop is created with value true to start the program loop.
         */
        boolean loop = true;

        while (loop) {
            out.print("\nPlease enter a value to find its square root: ");
            double input = in.nextDouble();
            /*
             * The user's input is entered as the parameter for the sqrt method
             */
            double answer = sqrt(input);
            /*
             * An output is printed to the user's screen with the square root
             * value.
             */
            out.println("The square root of " + input + " equals " + answer
                    + ".");
            /*
             * Asks if user wants to enter a new value, and continues loop if
             * user enters 'y'
             */
            out.print("\nEnter 'y' to compute a value's square root. Enter anything else to cancel the program: ");
            String repeat = in.nextLine();

            if (repeat.equals("y")) {
                loop = true;
            } else {
                loop = false;
            }
        }

        out.println("Program closed. Goodbye.");

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
