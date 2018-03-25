package org.team114.lib.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link EdgeDetector}.
 */
class EdgeDetectorTest  {

    private boolean value;
    private final EdgeDetector edgeDetector = new EdgeDetector(this::getValue);
    private boolean getValue() {
        return value;
    }

    /**
     * Tests the enum response of the getEdge function.
     */
    @Test
    void testGetEdge() {

        value = false;
        edgeDetector.update();
        value = true;
        assertEquals(edgeDetector.getEdge(), EdgeDetector.EdgeType.RISING);

        value = true;
        edgeDetector.update();
        value = false;
        assertEquals(edgeDetector.getEdge(), EdgeDetector.EdgeType.FALLING);

        value = false;
        edgeDetector.update();
        value = false;
        assertEquals(edgeDetector.getEdge(), EdgeDetector.EdgeType.FLAT);

        value = true;
        edgeDetector.update();
        value = true;
        assertEquals(edgeDetector.getEdge(), EdgeDetector.EdgeType.FLAT);
    }

    /**
     * Tests the boolean response of the falling (true -> false) function.
     */
    @Test
    void testFalling() {
        value = false;
        edgeDetector.update();
        value = true;
        assertFalse(edgeDetector.falling());

        value = true;
        edgeDetector.update();
        value = false;
        assertTrue(edgeDetector.falling());

        value = false;
        edgeDetector.update();
        value = false;
        assertFalse(edgeDetector.falling());

        value = true;
        edgeDetector.update();
        value = true;
        assertFalse(edgeDetector.falling());
    }

    /**
     * Tests the boolean response of the rising (false -> true) function.
     */
    @Test
    void testRising() {
        value = false;
        edgeDetector.update();
        value = true;
        assertTrue(edgeDetector.rising());

        value = true;
        edgeDetector.update();
        value = false;
        assertFalse(edgeDetector.rising());

        value = false;
        edgeDetector.update();
        value = false;
        assertFalse(edgeDetector.rising());

        value = true;
        edgeDetector.update();
        value = true;
        assertFalse(edgeDetector.rising());
    }

    /**
     * Tests the boolean response of the flatlining (true -> true | false -> false)
     * function.
     */
    @Test
    void testFlatlining() {
        value = false;
        edgeDetector.update();
        value = true;
        assertFalse(edgeDetector.flatlining());

        value = true;
        edgeDetector.update();
        value = false;
        assertFalse(edgeDetector.flatlining());

        value = false;
        edgeDetector.update();
        value = false;
        assertTrue(edgeDetector.flatlining());

        value = true;
        edgeDetector.update();
        value = true;
        assertTrue(edgeDetector.flatlining());
    }
}
