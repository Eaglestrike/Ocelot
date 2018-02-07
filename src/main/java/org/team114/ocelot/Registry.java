package org.team114.ocelot;

public interface Registry {
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
}
