package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

public interface RobotRegistry {
    <T> T get(String key);
    <T> T get(Class clazz);
    RobotSettings.Configuration getConfiguration();
}
