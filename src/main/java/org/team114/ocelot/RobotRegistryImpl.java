package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private class SubRobotRegistry implements RobotRegistry {
        private final RobotSettings.Configuration configuration;
        SubRobotRegistry(String prefix) {
            this.configuration = RobotRegistryImpl.this.getConfiguration(prefix);
        }
        @Override
        public <T> T get(String key) {
            return RobotRegistryImpl.this.get(key);
        }

        @Override
        public <T> T get(Class<? extends T> interfaceClazz) {
            return RobotRegistryImpl.this.get(interfaceClazz);
        }

        public RobotSettings.Configuration getConfiguration() {
            return this.configuration;
        }

        public RobotRegistry getRobotRegistry(String prefix) {
            return new SubRobotRegistry(configuration.getPrefix()+prefix);
        }
    };

    RobotRegistryImpl(RobotSettings robotSettings) {
        this.robotSettings = robotSettings;
    }

    public RobotRegistry getRobotRegistry(final String prefix) {
        return new SubRobotRegistry(prefix);
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
        return Optional.of((T) registryMap.get(key)).get();
    }

    void put(Object object) {
        singletonMap.put(object.getClass(), object);
    }

    /**
     * Puts the object into the singletonMap. The object is retrieved via {@link #get(Class)}
     * @param interfaceClazz interface implemented by object
     * @param object must be nonnull and implement interfaceClazz
     * @param <T>
     */
    <T> void put(Class<? extends T> interfaceClazz, T object) {
        singletonMap.put(interfaceClazz,
                Optional.of(object)
                .filter(obj -> interfaceClazz.isAssignableFrom(obj.getClass()))
                        .get());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAsList(final Class interfaceClazz) {
        return (List<T>) this.singletonMap.values().stream()
            .filter(obj -> interfaceClazz.isAssignableFrom(obj.getClass()))
                .collect(Collectors.toList());

    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends T> interfaceClazz) {
        return Optional.of((T) this.singletonMap.get(interfaceClazz)).get();
    }

    private RobotSettings.Configuration getConfiguration(String prefix) {
        return this.robotSettings.getConfiguration(prefix);
    }
    public RobotSettings.Configuration getConfiguration() {
        return this.robotSettings.getConfiguration("");
    }
}
