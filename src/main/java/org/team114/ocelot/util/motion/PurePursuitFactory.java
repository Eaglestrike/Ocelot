package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PurePursuitFactory {
    public static PathPointList loadPath(String pathName) {
        final String resourceBasePath = "/org/team114/ocelot/paths/";
        final InputStream inputStream = PurePursuitFactory.class.getResourceAsStream(resourceBasePath + pathName + ".114path");
        final InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader);

        final List<String> lines = reader.lines().collect(Collectors.toList());
        final int finishLineVecIndex = lines.size() - 1;
        final List<PathComponent> path = new ArrayList<>(lines.size() - 2);
        int goalPointIndex = 0;

        for (int i = 1; i < finishLineVecIndex - 1; i++) {
            String[] line = lines.get(i).split(",", -1);
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

        String[] vecLine = lines.get(finishLineVecIndex).split(",", -1);
        return new PathPointList(path, goalPointIndex, new Point(
                Double.parseDouble(vecLine[0]), // x
                Double.parseDouble(vecLine[1]) // y
        ));
    }

    public static PurePursuitController startPurePursuit(PathPointList path, double lookAheadDistance) {
        return new PurePursuitController(path, lookAheadDistance);
    }
}
