package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In more advance systems, we would use something like Guice or Springframework to do
 * Dependency injection. In such a system the registry is less visible.
 *
 * RobotRegistryImpl is our poorman dependency injection.
 *
 * No other object should hold a direct reference to another object held in the RobotRegistryImpl
 * Lookups are fast enough.
 */
class RobotRegistryImpl implements RobotRegistry {
    private final Map<String, Object> registryMap = new ConcurrentHashMap<>();
    private final Map<Class, Object> singletonMap = new ConcurrentHashMap<>();
    private final RobotSettings robotSettings;

    RobotRegistryImpl(RobotSettings robotSettings, String prefix) {
        this.robotSettings = robotSettings;
    }

    RobotRegistry getRobotRegistry(final String prefix) {
        @SuppressWarnings("unchecked")
        RobotRegistry robotRegistry = new RobotRegistry() {
            private RobotSettings.Configuration configuration = RobotRegistryImpl.this.getConfiguration(prefix);
            @Override
            public <T> T get(String key) {
                return RobotRegistryImpl.this.get(key);
            }

            @Override
            public <T> T get(Class<? extends T> clazz) {
                return RobotRegistryImpl.this.get(clazz);
            }

            public RobotSettings.Configuration getConfiguration() {
                return this.configuration;
            }
        };
        return robotRegistry;
    }

    void put(String key, Object object) {
        synchronized (registryMap) {
            if (this.registryMap.containsKey(key)) {
                throw new IllegalArgumentException("Key already in use " + key);
            }
            this.registryMap.put(key, object);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) registryMap.get(key);
    }

    void put(Object object) {
        singletonMap.put(object.getClass(), object);
    }

    <T> void put(Class<? extends T> clazz, T object) {
        singletonMap.put(clazz, object);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAsList(final Class clazz) {
        return (List<T>) this.singletonMap.values().stream()
        .filter(obj -> clazz.isAssignableFrom(obj.getClass()))
                .collect(Collectors.toList());

    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends T> clazz) {
        return (T) this.singletonMap.get(clazz);
    }

    private RobotSettings.Configuration getConfiguration(String prefix) {
        return this.robotSettings.getConfiguration(prefix);
    }
    public RobotSettings.Configuration getConfiguration() {
        return this.robotSettings.getConfiguration("");
    }
}
