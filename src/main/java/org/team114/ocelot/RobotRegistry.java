package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

public interface RobotRegistry {

    /**
     * allows RobotRegistry to return narrowed RobotRegistry
     * For example.
     * this is the robotregistry passed to drive:
     *   RobotRegistry.getSubRobotRegistry("Drive")
     *
     * the Drive code can then pass to gearshifter:
     *   this.robotRegistry.getSubRobotRegistry("GearShifter")
     *
     * the gearshifter can then ask for "highGearChannel" which really looks at the key:
     *  "Drive.GearShifter.highGearChannel"
     * property
     *
     * @param prefix (added to this' prefix)
     * @return
     */
    RobotRegistry getSubRobotRegistry(String prefix);
    /**
     *
     * @param key
     * @param <T>
     * @return guaranteed to be nonnull
     */
    <T> T get(String key);
    /**
     *
     * @param interfaceClazz
     * @return guaranteed to be nonnull
     */
    <T> T get(Class<? extends T> interfaceClazz);

    <K,V> V getIndex(K key, Class<? extends V> interfaceClazz);
    RobotSettings.Configuration getConfiguration();
}
