package org.team114.ocelot.subsystems.drive;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Used in events to indicate if the vent should apply to the left, right or both.
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
    
    @Override
    public Iterator<Side> iterator() {
        return this.sides.iterator();
    }
}
