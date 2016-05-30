import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

public class StringReassemblyTest {

    @Test
    public void addToSetAvoidingSubstringsTest1() {
        SimpleWriter out = new SimpleWriter1L();
        String test1 = "Hello World";
        String test2 = "Hello";
        Set<String> result = new Set1L<>();
        result.add(test1);

        StringReassembly.addToSetAvoidingSubstrings(result, test2);
        out.print(result);
        //Should print "{Hello World}"
        out.close();

    }

    @Test
    public void addToSetAvoidingSubstringsTest2() {
        SimpleWriter out = new SimpleWriter1L();
        String test1 = "Mississippi";
        String test2 = "issippi";
        Set<String> result = new Set1L<>();
        result.add(test1);

        StringReassembly.addToSetAvoidingSubstrings(result, test2);
        out.print(result);
        //Should print "{Mississippi}"
        out.close();

    }

    @Test
    public void addToSetAvoidingSubstringsTest3() {
        SimpleWriter out = new SimpleWriter1L();
        String test1 = "This?";
        String test2 = "What?";
        Set<String> result = new Set1L<>();
        result.add(test2);

        StringReassembly.addToSetAvoidingSubstrings(result, test1);
        out.print(result);
        //Should print "{This?, What?}"
        out.close();

    }

    @Test
    public void printWithLineSeparatorsTest1() {
        SimpleWriter out = new SimpleWriter1L();
        String test1 = "This ~ is ~ how ~ we ~ do ~ it";

        StringReassembly.printWithLineSeparators(test1, out);

        out.close();
    }

    @Test
    public void printWithLineSeparatorsTest2() {
        SimpleWriter out = new SimpleWriter1L();
        String test1 = "Here we will try ~ another one";

        StringReassembly.printWithLineSeparators(test1, out);

        out.close();
    }

    @Test
    public void combinationTest1() {

        String test1 = "She sells sea sh";
        String test2 = "shells by the sea shore";
        String resultExp = "She sells sea shells by the sea shore";

        int overlay = 2;

        String result = StringReassembly.combination(test1, test2, overlay);

        assertEquals(resultExp, result);

    }

    @Test
    public void combinationTest2() {

        String test1 = "Get o";
        String test2 = "on up!";
        String resultExp = "Get on up!";
        int overlay = 1;

        String result = StringReassembly.combination(test1, test2, overlay);

        assertEquals(resultExp, result);

    }

}