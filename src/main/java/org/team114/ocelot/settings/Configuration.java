package org.team114.ocelot.settings;

import org.team114.ocelot.logging.Errors;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Configuration {
    public static Configuration loadFromProperties() throws IOException {
        String filename = "/org/team114/ocelot/settings/Configuration.properties";
        System.out.println("Loading " + filename);
        InputStream io = Configuration.class.getResourceAsStream(filename);
        if (io == null) {
            throw new IOException("Could not load settings from " + filename);
        }
        return load(io);
    }

    public static Configuration load(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return new Configuration(properties);
    }

    /**
     * This registry ensures that channels are only allocated to a single purpose.
     * This prevents 2 different subsystems from accidentally trying to use the same channel.
     */
    private Map<Integer, String> channelRegistry = new ConcurrentHashMap<>();
    private Properties properties;
    private String prefix;

    public Configuration(Properties properties, String prefix) {
        this.properties = properties;
        this.prefix = prefix;
    }

    public Configuration(Properties properties) {
        this(properties, "");
    }

    public Configuration subConfiguration(String prefix) {
        if (this.prefix.isEmpty()) {
            return new Configuration(properties, prefix + ".");
        } else {
            return new Configuration(properties, this.prefix + prefix + ".");
        }
    }

    private String getKey(String key) {
        return prefix + key;
    }

    /**
     * Throws NoSuchElementException if the property value is missing
     * @param key throws NullPointerException if key is null
     * @return property as a string
     */
    public String getProperty(String key) {
        String nonnullKey = getKey(key);
        String property = properties.getProperty(nonnullKey);
        if (property == null) {
            throw new NoSuchElementException(nonnullKey);
        }
        return property;
    }

    public double getDouble(String key) {
        return Double.valueOf(getProperty(key));
    }

    /**
     * Use {@link #getChannelAndRegister(String)} if this is a channel id.
     * @param key
     * @return property as an int
     */
    public int getInt(String key) {
        return Integer.valueOf(getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }

    public List<Integer> getIntList(String key) {
        return getList(key, Integer::valueOf);
    }

    public <T> List<T> getList(String key, Function<String, T> transform) {
        String[] list = getProperty(key).split(",");
        return Arrays.asList(list).stream().map(transform).collect(Collectors.toList());
    }

    /**
     * @param key
     * @return
     * @throws IllegalStateException if the channel is already registered by a different key.
     */
    public int getChannelAndRegister(String key) {
        String nonnullKey = getKey(key);
        int channel = getInt(key);
        Errors.assertThat(channel > -1 && channel < Settings.MAX_NUMBER_OF_CHANNELS, () ->  nonnullKey + ": Channel must be between 0 and 7");
        channelRegistry.putIfAbsent(channel, nonnullKey);
        String registeredKey = channelRegistry.get(channel);
        Errors.assertThat(registeredKey.equals(nonnullKey),
                () -> "Duplicate access to channel "+ channel+ " by key " + registeredKey + " and " +nonnullKey);
        return channel;
    }
}
