package org.team114.lib.util;

/*
 * The original version of this code was released by FRC Team 254 under the MIT license.
 */

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;


/**
 * Used to get values at points that are not defined by making a guess from points that are
 * defined. This uses linear interpolation.
 * 
 * @param <K> The type of the key (must implement InverseInterpolable)
 * @param <V> The type of the value (must implement Interpolable)
 */
public class InterpolatingTreeMap<K extends InverseInterpolable<K> & Comparable<K>, V extends Interpolable<V>>
        extends TreeMap<K, V> {
    private static final long serialVersionUID = 8347275262778054124L;

    private int max;

    public InterpolatingTreeMap(int maximumSize) {
        max = maximumSize;
    }

    public InterpolatingTreeMap() {
        this(0);
    }

    /**
     * Inserts a key value pair, and trims the tree if a max size is specified
     * 
     * @param key Key for inserted data.
     * @param value Value for inserted data.
     * @return The value.
     */
    @Override
    public V put(K key, V value) {
        if (max > 0 && max <= size()) {
            // "Prune" the tree if it is oversize
            K first = firstKey();
            remove(first);
        }

        super.put(key, value);

        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        System.out.println("Unimplemented method");
    }

    /**
     *
     * @param key Key to check for a value (does not have to exist)
     * @return V or null; V if it is Interpolable or exists, null if it is at a bound and cannot average
     */
    @Nullable
    public V getInterpolated(K key) {
        V gotval = get(key);
        if (gotval == null) {
            // Get surrounding keys for interpolation
            K topBound = ceilingKey(key);
            K bottomBound = floorKey(key);

            //If attempting interpolation at ends of tree, return the nearest data point
            if (topBound == null && bottomBound == null) {
                return null;
            } else if (topBound == null) {
                return get(bottomBound);
            } else if (bottomBound == null) {
                return get(topBound);
            }

            // Get surrounding values for interpolation
            V topElem = get(topBound);
            V bottomElem = get(bottomBound);
            return bottomElem.interpolate(topElem, bottomBound.inverseInterpolate(topBound, key));
        } else {
            return gotval;
        }
    }
}