package org.team114.ocelot;

import junit.framework.TestCase;
import org.junit.Test;
import org.team114.ocelot.settings.RobotSettings;

import java.io.IOException;

public class TestRobotRegistry extends TestCase {
    @Test
    public void test() throws IOException {
        RobotRegistryImpl robotRegistry = new RobotRegistryImpl(new RobotSettings().load());
        int channelA = robotRegistry.getSubRobotRegistry("Drive")
                .getSubRobotRegistry("left")
                .getConfiguration().getInt("channelA");
        assertEquals(5, channelA);
    }
}
