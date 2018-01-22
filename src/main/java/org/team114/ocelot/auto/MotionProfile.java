package org.team114.ocelot.auto;

import org.team114.ocelot.settings.RobotSettings;

public class MotionProfile {

    private double time, initialPosition, finalPosition, initialVelocity, finalVelocity;

    public MotionProfile(double time, double initialPosition, double finalPosition, double initialVelocity, double finalVelocity) {
        this.time = time;
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
        this.initialVelocity = initialVelocity;
        this.finalVelocity = finalVelocity;
    }

    public double getVelocity() {
        // Time for one leg of perfect case
        double T0 = (RobotSettings.MAX_VELOCITY - initialVelocity) / RobotSettings.MAX_ACCELERATION;
        // Total distance for Perfect Case
        double distancePerfect = 2 * (initialVelocity * T0 + 0.5 * RobotSettings.MAX_ACCELERATION * T0 * T0);

        if (finalPosition - initialPosition <= distancePerfect) {
            // Time for one leg of triangular case
            double T1 = (-initialVelocity + Math.sqrt(initialVelocity * initialVelocity + RobotSettings.MAX_ACCELERATION * (finalPosition - initialPosition))) / RobotSettings.MAX_ACCELERATION;

            if (time >= 0 && time < T1) {
                return initialVelocity + RobotSettings.MAX_ACCELERATION * time;
            } else if (time > T1 && time < 2 * T1) {
                return ((initialVelocity + RobotSettings.MAX_ACCELERATION * T1) - RobotSettings.MAX_ACCELERATION * (time - T1));
            } else if (time < 0) {
                throw new RuntimeException("Negative time");
            } else {
                throw new RuntimeException("Already completed");
            }
        } else {
            double T2 = (RobotSettings.MAX_VELOCITY - finalVelocity) / RobotSettings.MAX_ACCELERATION;
            double P1 = initialPosition + (initialVelocity * T0) + ((RobotSettings.MAX_ACCELERATION * Math.pow(T0, 2)) / 2);
            double P2 = Math.pow(T2, 2) * (RobotSettings.MAX_ACCELERATION / 2);
            double middleDistance = finalPosition - P1 - P2;
            double T1 = (middleDistance / RobotSettings.MAX_VELOCITY) + T0;
            if (time >= 0 && time < T0) {
                return initialVelocity + (RobotSettings.MAX_ACCELERATION * time);
            } else if (time >= T0 && time < T1) {
                return RobotSettings.MAX_VELOCITY;
            } else if (time >= T1 && time <= T1 + T2) {
                return RobotSettings.MAX_VELOCITY - (RobotSettings.MAX_ACCELERATION * (time - T1));
            } else if (time < 0) {
                throw new RuntimeException("Negative time");
            } else {
                throw new RuntimeException("Already completed");
            }
        }
    }

    public double getAcceleration() {
        double T0 = (RobotSettings.MAX_VELOCITY - initialVelocity) / RobotSettings.MAX_ACCELERATION;
        double T2 = (RobotSettings.MAX_VELOCITY - finalVelocity) / RobotSettings.MAX_ACCELERATION;
        double P1 = initialPosition + (initialVelocity * T0) + ((RobotSettings.MAX_ACCELERATION * Math.pow(T0, 2)) / 2);
        double P2 = Math.pow(T2, 2) * (RobotSettings.MAX_ACCELERATION / 2);
        double middleDistance = finalPosition - P1 - P2;
        double T1 = (middleDistance / RobotSettings.MAX_VELOCITY) + T0;
        if (time >= 0 && time < T0) {
            return RobotSettings.MAX_ACCELERATION;
        } else if (time >= T0 && time < T1) {
            return 0;
        } else if (time >= T1 && time <= T1 + T2) {
            return -RobotSettings.MAX_ACCELERATION;
        } else if (time < 0) {
            throw new RuntimeException("Negative time");
        } else {
            throw new RuntimeException("Already completed");
        }
    }

    public double getPosition() {
        double T0 = (RobotSettings.MAX_VELOCITY - initialVelocity) / RobotSettings.MAX_ACCELERATION;
        double T2 = (RobotSettings.MAX_VELOCITY - finalVelocity) / RobotSettings.MAX_ACCELERATION;
        double P1 = initialPosition + (initialVelocity * T0) + ((RobotSettings.MAX_ACCELERATION * Math.pow(T0, 2)) / 2);
        double P2 = Math.pow(T2, 2) * (RobotSettings.MAX_ACCELERATION / 2);
        double middleDistance = finalPosition - P1 - P2;
        double T1 = (middleDistance / RobotSettings.MAX_VELOCITY) + T0;
        if (time >= 0 && time < T0) {
            return initialPosition + (initialVelocity * time) + ((RobotSettings.MAX_ACCELERATION * Math.pow(time, 2)) / 2);
        } else if (time >= T0 && time < T1) {
            return P1 + RobotSettings.MAX_VELOCITY * (time - T0);
        } else if (time >= T1 && time <= T1 + T2) {
            return (finalPosition - P2) + (RobotSettings.MAX_VELOCITY * (time - T1)) - ((RobotSettings.MAX_ACCELERATION * Math.pow(time - T1, 2)) / 2);
        } else if (time < 0) {
            throw new RuntimeException("Negative time");
        } else {
            throw new RuntimeException("Already completed");
        }
    }
}
