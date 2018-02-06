package org.team114.ocelot.util.motion;

import org.team114.lib.geometry.Point;

public class PathComponent {

    private Point location;
    private double distance;

    public PathComponent(Point p, double dist){
        location = p;
        distance = dist;
    }

    public Point getLocation(){
        return location;
    }

    public double getDistance(){
        return distance;
    }
}
