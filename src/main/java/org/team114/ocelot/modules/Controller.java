package org.team114.ocelot.modules;

import org.team114.ocelot.util.PercentageRange;

public interface Controller {

    PercentageRange throttle();

    PercentageRange wheel();

    boolean startLift();

    boolean endLift();

    boolean intake();

    boolean quickTurn();

    boolean startClimb();

    boolean endClimb();

}
