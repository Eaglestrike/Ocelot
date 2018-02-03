package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

public interface RobotRegistry {

    RobotRegistry getRobotRegistry(String prefix);
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
     * @param <T>
     * @return guaranteed to be nonnull
     */
    <T> T get(Class<? extends T> interfaceClazz);
    RobotSettings.Configuration getConfiguration();
}
