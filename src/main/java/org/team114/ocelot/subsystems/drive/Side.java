package org.team114.ocelot.subsystems.drive;

import java.util.Arrays;
import java.util.Iterator;

public enum Side implements Iterable<Side>{
    LEFT {
        @Override
        public Iterator<Side> iterator() {
            return Arrays.asList(LEFT).iterator();
        }
    },
    RIGHT {
        @Override
        public Iterator<Side> iterator() {
            return Arrays.asList(RIGHT).iterator();
        }
    },
    BOTH {
        @Override
        public Iterator<Side> iterator() {
            return Arrays.asList(LEFT, RIGHT).iterator();
        }
    }
}