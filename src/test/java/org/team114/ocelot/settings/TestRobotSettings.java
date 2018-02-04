package org.team114.ocelot.settings;

import junit.framework.TestCase;

import java.io.IOException;

public class TestRobotSettings extends TestCase {
    public void test() throws IOException {
        RobotSettings robotSettings = new RobotSettings().load();
        RobotSettings.Configuration config = robotSettings.getConfiguration("Drive");
        boolean b = config.getBoolean("left.reversedDirection");
        assertTrue(b);
        int cA = config.getChannelAndRegister("right.channelA");
        assertEquals(7, cA);
        double d = config.getDouble("right.distancePerPulseInFeet");
        System.out.println(d);
        System.out.println(robotSettings.getChannelRegistry().toString());
    }
}
