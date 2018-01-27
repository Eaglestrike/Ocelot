package org.team114.ocelot.auto;

import org.team114.ocelot.settings.RobotSettings;

public class MotionProfile {

    private double time, initialPosition, finalPosition, initialVelocity, finalVelocity;

    // Variables that are calculated based on arguments that are to be used by methods to calculate velocity,
    // position, and acceleration for a given time
    private double distancePerfect, middleDistance, T0, T1, T2, P1, P2;
    private boolean isTriangular;
    private double timePeriodOne, timePeriodTwo, timePeriodThree;

    public MotionProfile(double time, double initialPosition, double finalPosition,
                         double initialVelocity, double finalVelocity) {
        this.time = time;
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
        this.initialVelocity = initialVelocity;
        this.finalVelocity = finalVelocity;

        distancePerfect = 2 * (initialVelocity * T0 + 0.5 * RobotSettings.MAX_ACCELERATION * T0 * T0);
        T0 = (RobotSettings.MAX_VELOCITY - initialVelocity) / RobotSettings.MAX_ACCELERATION;

        if (finalPosition - initialPosition <= distancePerfect) {
            isTriangular = true;

            T1 = (-initialVelocity + Math.sqrt(initialVelocity * initialVelocity +
                    RobotSettings.MAX_ACCELERATION * (finalPosition - initialPosition))) /
                    RobotSettings.MAX_ACCELERATION;

            timePeriodOne = T1;
            timePeriodTwo = 2 * T1;
        } else {
            isTriangular = false;

            T2 = (RobotSettings.MAX_VELOCITY - finalVelocity) / RobotSettings.MAX_ACCELERATION;
            P1 = initialPosition + (initialVelocity * T0) + ((RobotSettings.MAX_ACCELERATION *
                    Math.pow(T0, 2)) / 2);

            P2 = Math.pow(T2, 2) * (RobotSettings.MAX_ACCELERATION / 2);
            middleDistance = finalPosition - P1 - P2;
            T1 = (middleDistance / RobotSettings.MAX_VELOCITY) + T0;

            timePeriodOne = T0;
            timePeriodTwo = T1;
            timePeriodThree = T1 + T2;
        }

        // Error handling
        if (time < 0) {
            throw new IllegalArgumentException("Negative time");
        }

        if ((isTriangular && time > timePeriodTwo) || (!isTriangular && time > timePeriodThree)) {
            throw new IllegalArgumentException("Already completed");
        }
    }
    public double getVelocity() {
        if (isTriangular) {
            if (time >= 0 && time < timePeriodOne) {
                return initialVelocity + RobotSettings.MAX_ACCELERATION * time;
            } else if (time >= timePeriodOne && time < timePeriodTwo) {
                return ((initialVelocity + RobotSettings.MAX_ACCELERATION * T1) -
                        RobotSettings.MAX_ACCELERATION * (time - T1));
            }
        } else {
            if (time >= 0 && time < timePeriodOne) {
                return initialVelocity + (RobotSettings.MAX_ACCELERATION * time);
            } else if (time >= timePeriodOne && time < timePeriodTwo) {
                return RobotSettings.MAX_VELOCITY;
            } else if (time >= timePeriodTwo && time <= timePeriodThree) {
                return RobotSettings.MAX_VELOCITY - (RobotSettings.MAX_ACCELERATION * (time - T1));
            }
        }

        throw new AssertionError("End of method should be unreachable");
    }

    public double getAcceleration() {
        if (time >= 0 && time < timePeriodOne) {
            return RobotSettings.MAX_ACCELERATION;
        } else if (time >= timePeriodOne && time < timePeriodTwo) {
            return 0;
        } else if (time >= timePeriodTwo && time <= timePeriodThree) {
            return -RobotSettings.MAX_ACCELERATION;
        }

        throw new AssertionError("End of method should be unreachable");
    }

    public double getPosition() {
        if (time >= 0 && time < timePeriodOne) {
            return initialPosition + (initialVelocity * time) + ((RobotSettings.MAX_ACCELERATION *
                    Math.pow(time, 2)) / 2);
        } else if (time >= timePeriodOne && time < timePeriodTwo) {
            return P1 + RobotSettings.MAX_VELOCITY * (time - T0);
        } else if (time >= timePeriodTwo && time <= timePeriodThree) {
            return (finalPosition - P2) + (RobotSettings.MAX_VELOCITY * (time - T1)) -
                    ((RobotSettings.MAX_ACCELERATION * Math.pow(time - T1, 2)) / 2);
        }

        throw new AssertionError("End of method should be unreachable");
    }
}
