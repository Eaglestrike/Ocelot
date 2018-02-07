package org.team114.ocelot;

import org.team114.ocelot.settings.Settings;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In more advance systems, we would use something like Guice or Springframework to do
 * Dependency injection. In such a system the registry is less visible.
 *
 * RegistryImpl is our poorman dependency injection.
 *
 * No other object should hold a direct reference to another object held in the RegistryImpl
 * Lookups are fast enough.
 */
class RegistryImpl implements Registry {
    private final Map<String, Object> registryMap = new ConcurrentHashMap<>();
    private final Map<Class, Object> objectsByClassMap = new ConcurrentHashMap<>();
    private final Settings settings;

    private class SubRegistry implements Registry {
        private final Settings.Configuration configuration;
        SubRegistry(String prefix) {
            this.configuration = RegistryImpl.this.getConfiguration(prefix);
        }
        @Override
        public <T> T get(String key) {
            return RegistryImpl.this.get(key);
        }

        @Override
        public <T> T get(Class<? extends T> interfaceClazz) {
            return RegistryImpl.this.get(interfaceClazz);
        }

        @Override
        public <K,V> V getIndex(K key, Class<? extends V> interfaceClazz) {
            return RegistryImpl.this.getIndex(key, interfaceClazz);
        }

        public Settings.Configuration getConfiguration() {
            return this.configuration;
        }

        public Registry getSubRegistry(String prefix) {
            return new SubRegistry(configuration.getPrefix()+prefix);
        }
    };

    RegistryImpl(Settings settings) {
        this.settings = settings;
    }

    public Registry getSubRegistry(final String prefix) {
        return new SubRegistry(prefix);
    }

    @SuppressWarnings("unchecked")
    <K, V> void putIndex(K key, V object) {
        objectsByClassMap.putIfAbsent(object.getClass(), new ConcurrentHashMap<K, V>());
        ConcurrentHashMap<K, V> subMap = (ConcurrentHashMap<K, V>) objectsByClassMap.get(object.getClass());
        subMap.put(key, object);
    }

    public <K,V> V getIndex(K key, Class<? extends V> interfaceClazz) {
        return ((Map<K,V>)get(interfaceClazz)).get(key);
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
        objectsByClassMap.put(object.getClass(), object);
    }

    /**
     * Puts the object into the objectsByClassMap. The object is retrieved via {@link #get(Class)}
     * @param interfaceClazz interface implemented by object
     * @param object must be nonnull and implement interfaceClazz
     * @param <T>
     */
    <T> void put(Class<? extends T> interfaceClazz, T object) {
        objectsByClassMap.put(interfaceClazz,
                Optional.of(object)
                .filter(obj -> interfaceClazz.isAssignableFrom(obj.getClass()))
                        .get());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAsList(final Class interfaceClazz) {
        return (List<T>) this.objectsByClassMap.values().stream()
            .filter(obj -> interfaceClazz.isAssignableFrom(obj.getClass()))
                .collect(Collectors.toList());

    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<? extends T> interfaceClazz) {
        return Optional.of((T) this.objectsByClassMap.get(interfaceClazz)).get();
    }

    private Settings.Configuration getConfiguration(String prefix) {
        return this.settings.getConfiguration(prefix);
    }
    public Settings.Configuration getConfiguration() {
        return this.settings.getConfiguration(null);
    }
}
