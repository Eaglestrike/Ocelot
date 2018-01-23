package org.team114.ocelot.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used in events to indicate if the event should apply to the left, right or both.
 */
public enum Side implements Iterable<Side> {
    LEFT,
    RIGHT,
    BOTH(Arrays.asList(LEFT, RIGHT));

    private final List<Side> sides;

    Side() {
        this.sides = Collections.singletonList(this);
    }

    Side(List<Side> sides) {
        this.sides = sides;
    }

    /**
     * Allows the user to target the Side or Sides as represented by this enum instance.
     * In particular note that an event with BOTH can easily target both drive sides.
     * @return An iterator of the sides this enum represents.
g     */
    @Override
    public Iterator<Side> iterator() {
        return this.sides.iterator();
    }
}
