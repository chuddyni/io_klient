package domeny.niema.klient;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

public class StringUtilsTestUnit5 {

    @Test
    public void testConvertToDoubleOK() {
        String age = "1990";
        Double expAge = Double.valueOf(age);
        Double actual = StringUtils.convertToDouble(age);

        assertAll("Do many assertions.", () -> {
            assertNotNull(actual);
            assertEquals(expAge, actual);
        });


        assertAll("Do many assertions.Java 8 Lambdas style", () -> {
            assertNotNull(actual, () -> "The actual is NULL");
            assertEquals(expAge, actual,
                    () -> "The expected is: " + expAge + " while the actual is:" + actual);
        });

    }

    @Test
    public void testConvertToDoubleWithNullArgument() {
        String age = null;
        Double actual = StringUtils.convertToDouble(age);
        assertNull(actual, "The actual is not null");
        assertNull(actual, () -> "The actual is not null");
    }



    @Test
    public void testConvertToDoubleThrowException() {
        String age = "N/A";
        assertThrows(NumberFormatException.class, () -> {
            StringUtils.convertToDouble(age);
        });

        assertThrows(NumberFormatException.class, () -> {
            StringUtils.convertToDouble(age);
        });
    }

    @Test
    public void testIsNullOrBlankOK() {
        String input = null;

        assertTrue(StringUtils.isNullOrBlank(input));
        assertTrue(StringUtils.isNullOrBlank(input), () -> "The string is not null or blank");

        input = " ";
        assertTrue(StringUtils.isNullOrBlank(input));

        input = "abc";

        assertFalse(StringUtils.isNullOrBlank(input));

    }

    @Test
    public void testGetDefaultIfNull() {

        String st = null;
        String defaultSt = "abc";

        String actual = StringUtils.getDefaultIfNull(st, defaultSt);
        assertSame(defaultSt, actual);

        assertSame(defaultSt, actual, () -> "Expected ouput is not same with actual");

        st = "def";

        actual = StringUtils.getDefaultIfNull(st, defaultSt);
        assertNotSame(defaultSt, actual);

        assertNotSame(defaultSt, actual, () -> "Expected ouput is same with actual");

        st = "";
        actual = StringUtils.getDefaultIfNull(st, defaultSt);
        if (actual.equals(defaultSt)) {
            fail("The actual should be empty");
            fail(() -> "The actual should be empty");
        }

    }

    @Test
    public void testConcatWithRegularInput() {
        String st1 = "Hello";
        String st2 = "World";
        String st3 = "!";
        String expect = st1 + st2 + st3;
        String actual = StringUtils.concat(st1, st2, st3);
        assertEquals(expect, actual);
    }

    @Test
    public void testConcatWithNullInput() {
        String st1 = "Hello";
        String st2 = "World";
        String st3 = null;
        String expect = st1 + st2;
        String actual = StringUtils.concat(st1, st2, st3);
        assertEquals(expect, actual);
    }

    @Disabled
    @Test
    public void testConcatWithAllNullInput() {
        String actual = StringUtils.concat();
        assertNull(actual);
    }
}