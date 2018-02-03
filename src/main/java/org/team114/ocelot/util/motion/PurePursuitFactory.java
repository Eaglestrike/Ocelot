package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PurePursuitFactory {

    public static PathPointList loadPath(String pathName) {
        String resourceBasePath = "/org/team114/ocelot/paths/";
        InputStream inputStream = PurePursuitFactory.class.getResourceAsStream(resourceBasePath + pathName + ".csv");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);

        List<PathComponent> components = reader.lines()
            .skip(1) // skip the header line
            .map(line -> line.split(","))
            .map(line -> new PathComponent(
                new Point(
                    Double.parseDouble(line[0]), // x
                    Double.parseDouble(line[1]) // y
                ), Double.parseDouble(line[2]) // distance
            ))
            .collect(Collectors.toList());

        return new PathPointList(components);
    }

    public static PurePursuitController startPurePursuit(PathPointList path, double lookAheadDistance, double finishMargin) {
        return startPurePursuit(path, lookAheadDistance, finishMargin, 0);
    }

    public static PurePursuitController startPurePursuit(PathPointList path, double lookAheadDistance, double finishMargin, double finalVelocity) {
        return new PurePursuitController(path, lookAheadDistance, finishMargin, finalVelocity);
    }
}
