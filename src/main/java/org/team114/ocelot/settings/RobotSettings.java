package org.team114.ocelot.settings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class RobotSettings {
    private Properties properties;

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

        public String getProperty(String key) {
            return RobotSettings.this.properties.getProperty(prefix+key);
        }

        public double getDouble(String key) {
            return Double.valueOf(getProperty(key));
        }

        public int getInt(String key) {
            return Integer.valueOf(getProperty(key));
        }

        public boolean getBoolean(String key) {
            return Boolean.valueOf(getProperty(key));
        }
    }

    public Configuration getConfiguration(String prefix) {
        return new Configuration(prefix);
    }

    public static void main(String[] argv) {
        try {
            RobotSettings robotSettings = new RobotSettings().load();
            Configuration config = robotSettings.getConfiguration("Drive");
            boolean b = config.getBoolean("left.reversedDirection");
            System.out.println(b);
            int cA = config.getInt("right.channelA");
            System.out.println(cA);
            double d = config.getDouble("right.distancePerPulseInFeet");
            System.out.println(d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final double WHEEL_DIAMETER_FT = 2.0/3.0;

    public static final double MAX_VELOCITY = 1;
    public static final double MAX_ACCELERATION = 2;
}
