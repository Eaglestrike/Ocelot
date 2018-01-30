package org.team114.ocelot.modules;

public interface Controller {

    double throttle();

    double wheel();

    boolean startLift();

    boolean endLift();

    boolean intake();

    boolean quickTurn();

    boolean startClimb();

    boolean endClimb();

}
