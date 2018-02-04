package org.team114.ocelot;

import org.team114.ocelot.settings.RobotSettings;

import java.io.IOException;

public class TestRobotRegistry {
    public static void main(String[] argv) throws IOException {
        RobotRegistryImpl robotRegistry = new RobotRegistryImpl(new RobotSettings().load());
        int channelA = robotRegistry.getRobotRegistry("Drive")
                .getRobotRegistry("left")
                .getConfiguration().getInt("channelA");
        System.out.println(channelA);

    }
}
