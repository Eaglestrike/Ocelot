package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;
import org.team114.lib.pathgenerator.Path;
import org.team114.lib.util.Epsilon;

import java.util.ArrayList;
import java.util.List;

/**
 * An used to save the PathComponent list using gson.
 */
public class PathPointList {

    public List<PathComponent> pathComponentList;

    public PathPointList(List<PathComponent> pathComponentList){
        this.pathComponentList = pathComponentList;
    }

    public PathComponent get(int i) {
        return this.pathComponentList.get(i);
    }

    public static PathPointList generate(Path p, double segmentDistance) {
        if (segmentDistance <= 0.0) {
            throw new IllegalArgumentException("Segment distance must be positive!");
        }
        List<PathComponent> pointList = new ArrayList<>();
        double tIndex = 0;
        double cumDistance = 0;
        Point lastPoint;

        lastPoint = p.getPointAtT(tIndex);
        pointList.add(new PathComponent(lastPoint, cumDistance));

        while (tIndex < p.length()) {
            Point dydx = p.dydx(tIndex);
            if (dydx.x() == 0.0 && dydx.y() == 0.0) {
                tIndex += Epsilon.EPSILON;
            } else {
                double speed = Math.sqrt(Math.pow(dydx.y(), 2) +  Math.pow(dydx.x(), 2));
                tIndex += segmentDistance / speed;
            }
            Point newPoint = p.getPointAtT(tIndex);
            cumDistance += lastPoint.dist(newPoint);
            lastPoint = newPoint;

            pointList.add(new PathComponent(newPoint, cumDistance));
        }

        return new PathPointList(pointList);
    }
}
