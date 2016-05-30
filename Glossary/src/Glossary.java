import components.map.Map;
import components.map.Map1L;
import components.sequence.Sequence;
import components.sequence.Sequence1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Put a short phrase describing the program here.
 *
 * @author Martin J Hobby
 *
 */
public final class Glossary {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
    }

    /**
     * Generates the header of the output HTML file opening html, head, title,
     * and body
     *
     * @param out
     *            the output stream
     * @param terms
     *            the sequence of terms in alphabetical order
     * @param mappedTerms
     *            the map of the terms with their associated definitions
     * @param termArray
     *            the list of terms in array format
     * @param outputFile
     *            the output file location selected by the client
     */
    private static void outputIndex(SimpleWriter out, Sequence<String> terms,
            Map<String, String> mappedTerms, String[] termArray,
            String outputFile) {

        /*
         * Takes the user provided output file name and creates the named
         * location of the html index
         */
        String index = outputFile + "/index.html";
        SimpleWriter indexPage = new SimpleWriter1L(index);

        /*
         * Generates all the header html code
         */
        indexPage.println("<html>");
        indexPage.println("<head>");
        indexPage.println("<title>" + "Glossary Index" + "</title>");
        indexPage.println("</head>");
        indexPage.println("<body>");
        indexPage.println("<p style=\"font-size:32pt;\">Glossary</p>");
        indexPage.println();
        indexPage.println("<p style=\"font-size:18pt;\">Index</p>");
        indexPage.println("<ul>");

        /*
         * loops through the list of terms generating each term page and
         * creating a link to the newly created page
         */
        while (terms.length() > 0) {
            String word = terms.remove(0);
            generateWordPages(word, out, mappedTerms, termArray, outputFile);
            indexPage.println("<li><a href=\"" + word + ".html\">" + word
                    + "</a></li>");
        }

        /*
         * Generates footer html code
         */
        indexPage.println("</ul>");
        indexPage.println("</body>");
        indexPage.println("</html>");

        indexPage.close();

    }

    /**
     * Generates the pages for each of the terms from the input file
     *
     * @param word
     *            the term the page is created around
     * @param out
     *            the output stream
     * @param mappedTerms
     *            the map of the terms and their definitions
     * @param termArray
     *            all of the terms that are in the input file
     * @param outputFile
     *            the output file location
     */
    private static void generateWordPages(String word, SimpleWriter out,
            Map<String, String> mappedTerms, String[] termArray,
            String outputFile) {

        /*
         * Generates string with location of the file as well as the html file's
         * page
         */
        String wordHtmlPage = outputFile + "/" + word + ".html";
        SimpleWriter htmlPage = new SimpleWriter1L(wordHtmlPage);

        htmlPage.println("<html>");
        htmlPage.println("<head>");
        htmlPage.println("<title>" + word + "</title>");
        htmlPage.println("</head>");
        htmlPage.println("<body>");
        htmlPage.println("<p style=\"color:red;\"><b><i>" + word
                + "</b></i></p>");
        htmlPage.println();

        /*
         * Grabs the word's definition. Then generateElements method goes
         * character by character returning the definition while also checking
         * if other word's in the list are found in the definition
         */
        String definition = mappedTerms.value(word);
        Set<Character> separatorSet = new Set1L<>();
        String separators = " ,";
        generateElements(separators, separatorSet);

        String pageInput = "";

        int i = 0;
        while (i < definition.length()) {
            String token = nextWordOrSeparator(definition, i, separatorSet);
            if (separatorSet.contains(token.charAt(0))) {
                pageInput = pageInput + token;
            } else {
                int n = 0;
                int count = 0;
                while (n < termArray.length) {
                    if (token.equals(termArray[n])) {
                        pageInput = pageInput + "<a href=\"" + termArray[n]
                                + ".html\">" + token + "</a>";
                        count++;
                    }
                    n++;
                }
                if (count == 0) {
                    pageInput = pageInput + token;
                }
            }
            i += token.length();
        }

        htmlPage.println("<p style=\"text-align:center;\">" + pageInput
                + "</p>");
        htmlPage.println();
        htmlPage.println("Return to <a href=\"index.html\">index</a>");
        htmlPage.println("</body>");
        htmlPage.println("</html>");

        htmlPage.close();
    }

    /**
     * Adds the terms and definitions to two sequences
     *
     * @param in
     *            the input stream
     * @param mapTerms
     *            the map that will contain the terms with their associated
     *            definitions
     * @param terms
     *            a set of only the terms
     */
    private static void findTermsAndDefinitions(SimpleReader in,
            Map<String, String> mapTerms, Set<String> terms) {

        while (!in.atEOS()) {

            String next = in.nextLine();
            String definition = "";
            String term = "";
            boolean emptyLine = true;

            /*
             * An empty line will prevent the loop from running, whereas the
             * next word that is found will be assigned to String term
             */
            if (next.equals("")) {

                emptyLine = false;
            } else {

                term = next;
            }

            while (emptyLine && !in.atEOS()) {
                /*
                 * Until empty line or end of input file, all the following
                 * words will be assigned to definition
                 */
                next = in.nextLine();
                if (!next.equals("")) {
                    definition = definition + " " + next;
                } else {
                    emptyLine = false;
                }
            }
            /*
             * terms and their associated definitions are then assigned to
             * mapTerms
             */
            mapTerms.add(term, definition);
            terms.add(term);
        }

    }

    /**
     * Takes the set of terms and puts them in alphabetical order
     *
     * @param terms
     *            the given set of terms
     * @replaces terms
     * @ensures terms = original terms set minus the lowest value word (first
     *          alphabetically)
     */
    private static String alphabeticalOrder(Set<String> terms) {

        Set<String> terms2 = new Set1L<>();
        String result = "";

        /*
         * as long as there are terms in the set and the result value is
         * 'blank', the extracted word is compared to all other terms in the
         * set. As long as the next word doesn't return a value greater than or
         * equal to 0 then it will be the next word alphabetically. ANy words
         * that fail are added to an additional set and then returned to the
         * original set.
         */
        while (terms.size() > 0 && result.equals("")) {
            int count = 0;
            String test = terms.removeAny();
            for (String word : terms) {
                if (word.compareTo(test) < 0) {
                    count++;
                }
            }
            if (count == 0) {
                result = test;
            } else {
                terms2.add(test);
            }
        }
        terms.add(terms2);
        return result;
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        int i = str.length();

        while (i > 0) {
            char x = str.charAt(i - 1);
            if (!strSet.contains(x)) {
                strSet.add(x);
            }
            i--;
        }

    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        char nextChar = text.charAt(position);
        String result = "" + nextChar;
        boolean sepTest = separators.contains(nextChar);
        int endStr = position;

        if (sepTest) {
            endStr++;
            if (endStr <= text.length()) {
                nextChar = text.charAt(endStr);
                while (separators.contains(nextChar)) {

                    if (!separators.contains(nextChar)) {
                        sepTest = false;
                    }
                    endStr++;
                    nextChar = text.charAt(endStr);
                }
            }

        }

        else {
            endStr++;
            if (endStr < text.length()) {
                nextChar = text.charAt(endStr);
                while (!separators.contains(nextChar)
                        && endStr != text.length()) {

                    if (separators.contains(nextChar)) {
                        sepTest = true;
                    }
                    endStr++;
                    if (endStr < text.length()) {
                        nextChar = text.charAt(endStr);
                    }
                }
            }
        }
        result = text.substring(position, endStr);

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

        /*
         * Prompts user for input file name as well as the output destination
         */
        out.print("Input the file to import: ");
        String inputFileName = in.nextLine();
        SimpleReader inFile = new SimpleReader1L(inputFileName);
        out.print("Provide the output destination: ");
        String outputFile = in.nextLine();

        /*
         * sets matching terms and definitions from input file to mapTerms
         */
        Map<String, String> mapTerms = new Map1L<>();
        Set<String> terms = new Set1L<>();
        findTermsAndDefinitions(inFile, mapTerms, terms);

        /*
         * Sequence and array created with the terms in alphabetical order
         */
        Sequence<String> termsList = new Sequence1L<>();
        String[] termArray = new String[terms.size()];
        int i = 0;
        while (0 < terms.size()) {
            String nextTerm = alphabeticalOrder(terms);
            termsList.add(termsList.length(), nextTerm);
            termArray[i] = nextTerm;
            i++;
        }

        /*
         * generates html from termList, mapTerms, and termArray
         */
        outputIndex(out, termsList, mapTerms, termArray, outputFile);

        in.close();
        out.close();
    }

}
