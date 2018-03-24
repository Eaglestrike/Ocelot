package org.team114.lib.pathgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PolynomialSplineTest {

    private PolynomialSpline p = new PolynomialSpline(new double[]{7, 3, 5, 15, 2.2, -5.5, 0.0145, -0.234});

    @Test
    public void testBoundsCheckingValues() {
        // must not throw
        p.at(0);
        p.at(0.5);
        p.at(1);

        // must throw
        assertThrows(IndexOutOfBoundsException.class, () -> p.at(-0.1), "Must throw for negative inputs.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.at(-5), "Must throw for negative inputs.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.at(1.1),
                "Must throw for inputs greater than 1.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.at(5),
                "Must throw for inputs greater than 1.");

    }

    @Test
    public void testBoundsCheckingDerivatives() {

        //must not throw
        p.dfdt(0);
        p.dfdt(0.5);
        p.dfdt(1);


        // must throw
        assertThrows(IndexOutOfBoundsException.class, () -> p.dfdt(-0.1), "Must throw for negative inputs.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.dfdt(-5), "Must throw for negative inputs.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.dfdt(1.1),
                "Must throw for inputs greater than 1.");
        assertThrows(IndexOutOfBoundsException.class, () -> p.dfdt(5),
                "Must throw for inputs greater than 1.");
    }
}
