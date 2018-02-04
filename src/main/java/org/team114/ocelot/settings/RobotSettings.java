package org.team114.ocelot.settings;

import org.team114.ocelot.logging.Errors;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public final class RobotSettings {
    public static final int MAX_NUMBER_OF_CHANNELS = 8;
    private Properties properties;
    /**
     * This registry ensures that channels are only allocated to a single purpose.
     * This prevents 2 different subsystems from accidently trying to use the same channel.
     */
    private Map<Integer, String> channelRegistry = new ConcurrentHashMap<>();

    public RobotSettings load() throws IOException {
        String filename = "/"+this.getClass().getCanonicalName().replaceAll("\\.", "/") + ".properties";
        System.out.println("Loading " + filename);
        try (InputStream io =
                     this.getClass().getResourceAsStream(filename)) {
            if (io == null) {
                throw new IOException("Could not load settings from "+ filename);
            } else {
                this.properties = new Properties();
                this.properties.load(io);
            }
        }
        return this;
    }

    public class Configuration {
        private final String prefix;

        private Configuration(String prefix) {
            this.prefix = prefix == null ? "" : prefix + ".";
        }

        private String getKey(String key) {
            return Optional.of(key).map(nonnullKey -> prefix + nonnullKey).get();
        }
        /**
         * Throws NoSuchElementException if the property value is missing
         * @param key throws NullPointerException if key is null
         * @return property as a string
         */
        public String getProperty(String key) {
            String nonnullKey = getKey(key);
            String property = RobotSettings.this.properties.getProperty(nonnullKey);
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
         * @return
         */
        public int getInt(String key) {
            return Integer.valueOf(getProperty(key));
        }

        public boolean getBoolean(String key) {
            return Boolean.valueOf(getProperty(key));
        }

        /**
         * @param key
         * @return
         * @throws IllegalStateException if the channel is already registered by a different key.
         */
        public int getChannelAndRegister(String key) {
            String nonnullKey = getKey(key);
            int channel = getInt(key);
            Errors.assertThat(channel > -1 && channel < MAX_NUMBER_OF_CHANNELS, () ->  nonnullKey + ": Channel must be between 0 and 7");
            channelRegistry.putIfAbsent(channel, nonnullKey);
            String registeredKey = channelRegistry.get(channel);
            Errors.assertThat(registeredKey.equals(nonnullKey),
                        () -> "Duplicate access to channel "+ channel+ "by key " + registeredKey + " and " +nonnullKey);
            return channel;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }

    public Configuration getConfiguration(String prefix) {
        return new Configuration(prefix);
    }

    public Map<Integer, String> getChannelRegistry() {
        return new HashMap<>(this.channelRegistry);
    }

    public static final double WHEEL_DIAMETER_FT = 2.0/3.0;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
    public static final double CLIMBING_TIME = 10;
    public static final double GAME_TIME = 180;
}
