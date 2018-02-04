package org.team114.ocelot.settings;

import java.io.IOException;

public class TestRobotSettings {
    public static void main(String[] argv) {
        try {
            RobotSettings robotSettings = new RobotSettings().load();
            RobotSettings.Configuration config = robotSettings.getConfiguration("Drive");
            boolean b = config.getBoolean("left.reversedDirection");
            System.out.println(b);
            int cA = config.getChannelAndRegister("right.channelA");
            System.out.println(cA);
            double d = config.getDouble("right.distancePerPulseInFeet");
            System.out.println(d);
            System.out.println(robotSettings.getChannelRegistry().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
