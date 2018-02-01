package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class PurePursuitFactory {

    public static PurePursuitController startPurePursuit(String pointFileLocation, double lookAheadDistance, double finishMargin) throws FileNotFoundException {
        return startPurePursuitVf(pointFileLocation, lookAheadDistance, finishMargin, 0);
    }

    public static PurePursuitController startPurePursuitVf(String pointFileLocation, double lookAheadDistance, double finishMargin, double finalVelocity) throws FileNotFoundException {
        PathPointList path = new PathPointList(new ArrayList<PathComponent>());
        try {
            String line = null;
            BufferedReader in = new BufferedReader(new FileReader(pointFileLocation));
            while ((line = in.readLine()) != null) {
                String[] section = line.split(",");
                if (section.length != 3) continue;
                path.pathComponentList.add(new PathComponent(new Point(Double.parseDouble(section[0]),
                        Double.parseDouble(section[1])), Double.parseDouble(section[2])));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return new PurePursuitController(lookAheadDistance, path, finishMargin);
    }
}
