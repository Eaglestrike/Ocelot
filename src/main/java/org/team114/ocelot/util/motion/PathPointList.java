package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

import java.util.List;

/**
 * Holds information necessary for a Pure-Pursuit controller to follow its path.
 */
public class PathPointList {

    public List<PathComponent> mainPathPoints;
    public final PathComponent goalComponent;
    public final int goalComponentIndex;
    public final Point finishVector;


    public PathPointList(List<PathComponent> pathComponentList, int goalPointIndex, Point finishVector) {
        this.mainPathPoints = pathComponentList;
        this.goalComponentIndex = goalPointIndex;
        this.goalComponent = this.get(goalPointIndex);
        this.finishVector = finishVector;
    }

    public boolean isPastFinishLine(Point test) {
        // if the dot-produce is positive, we're past the line (SO ROBUST)
        Point goalToTest = new Point(
                test.x() - goalComponent.getLocation().x(),
                test.y() - goalComponent.getLocation().y()
        );
        return ((finishVector.x() * goalToTest.x()) + (finishVector.y() * goalToTest.y())) > 0;
    }

    public PathComponent get(int i) {
        return this.mainPathPoints.get(i);
    }
}
