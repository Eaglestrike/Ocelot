package org.team114.ocelot.util.motion;

import org.team114.ocelot.util.Pose;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.*;

public abstract class PurePursuitFactory {

    public static PurePursuitController startPurePursuit(String pointFileLocation, Pose initial) throws FileNotFoundException {
        return startPurePursuitVf(pointFileLocation, 0);
    }

    public static PurePursuitController startPurePursuitVf(String pointFileLocation, double finalVelocity) throws FileNotFoundException {
        PathPointList path;
        try {
            String line = null;
            BufferedReader in = new BufferedReader(new FileReader(pointFileLocation));
            StringBuilder s = new StringBuilder();
            while ((line = in.readLine()) != null)
                s.append(line);
            Gson gson = new Gson();
            path = gson.fromJson(s.toString(), PathPointList.class);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (path == null || path.pathComponentList.size() < 1)
            throw new FileNotFoundException("PathComponents file was either empty or null");

        PurePursuitController controller = new PurePursuitController(1, path, 0.5);
        return controller;
    }
}
