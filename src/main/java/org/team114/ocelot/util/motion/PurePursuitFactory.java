package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PurePursuitFactory {
    public static PathPointList loadPath(String pathName) {
        String resourceBasePath = "/org/team114/ocelot/paths/";
        InputStream inputStream = PurePursuitFactory.class.getResourceAsStream(resourceBasePath + pathName + ".114path");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        String[] lines = (String[])(reader.lines().toArray());
        int finishLineVecIndex = lines.length-1;
        List<PathComponent> path = new ArrayList<>(lines.length-2);
        int goalPointIndex = 0;
        for (int i = 1; i < finishLineVecIndex-1; i++) {
            String[] line = lines[i].split(",");
            path.add(new PathComponent(
                    new Point(
                            Double.parseDouble(line[0]), // x
                            Double.parseDouble(line[1]) // y
                    ), Double.parseDouble(line[2]) // distance
            ));
            // if we're not in linear-interp land update the goal point
            if (!Boolean.parseBoolean(line[3])) {
                goalPointIndex = i;
            }
        }
        String[] vecLine = lines[finishLineVecIndex].split(",");
        return new PathPointList(path, goalPointIndex, new Point(
                Double.parseDouble(vecLine[0]), // x
                Double.parseDouble(vecLine[1]) // y
        ));
    }

    public static PurePursuitController startPurePursuit(PathPointList path, double lookAheadDistance, double finishMargin) {
        return startPurePursuit(path, lookAheadDistance, finishMargin, 0);
    }

    public static PurePursuitController startPurePursuit(PathPointList path, double lookAheadDistance, double finishMargin, double finalVelocity) {
        return new PurePursuitController(path, lookAheadDistance, finishMargin, finalVelocity);
    }
}
