package org.team114.lib.pathgenerator;

import org.junit.*;

import static org.junit.Assert.*;

public class PolynomialSplineTest {

    @Test
    public void testBoundsChecking() {
        PolynomialSpline p = new PolynomialSpline(new double[]{7, 3, 5, 15, 2.2, -5.5, 0.0145, -0.234});

        //for values
        try {
            p.at(0);
            p.at(0.5);
            p.at(1);
        } catch (Exception e) {
            fail();
        }

        try {
            p.at(-0.1);
            fail();
        } catch (Exception e) {
        }

        try {
            p.at(1.1);
            fail();
        } catch (Exception e) {
        }

        try {
            p.at(5);
            fail();
        } catch (Exception e) {
        }

        try {
            p.at(-5);
            fail();
        } catch (Exception e) {
        }

        //for derivatives
        try {
            p.dfdt(0);
            p.dfdt(0.5);
            p.dfdt(1);
        } catch (Exception e) {
            fail();
        }

        try {
            p.dfdt(-0.1);
            fail();
        } catch (Exception e) {
        }

        try {
            p.dfdt(1.1);
            fail();
        } catch (Exception e) {
        }

        try {
            p.dfdt(5);
            fail();
        } catch (Exception e) {
        }

        try {
            p.dfdt(-5);
            fail();
        } catch (Exception e) {
        }
    }
}
