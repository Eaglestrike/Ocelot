package org.team114.ocelot;

import org.team114.ocelot.settings.Settings;

public interface Registry {

    /**
     * allows Registry to return narrowed Registry
     * for example.
     * this is the registry passed to drive:
     *   Registry.getSubRegistry("Drive")
     *
     * the Drive code can then pass to gearshifter:
     *   this.registry.getSubRegistry("GearShifter")
     *
     * the gearshifter can then ask for "highGearChannel" which really looks at the key:
     *  "Drive.GearShifter.highGearChannel"
     * property
     *
     * @param prefix (added to this' prefix)
     * @return
     */
    Registry getSubRegistry(String prefix);
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
    Settings.Configuration getConfiguration();
}
