package org.team114.ocelot.auto;

public class MotionProfile {
    // Dan told me that these should be static for the robot. TODO change these values
    public static final double maxVelocity = 1;
    public static final double maxAcceleration = 1;

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
        double T0 = (maxVelocity - initialVelocity) / maxAcceleration;
        // Total distance for Perfect Case
        double distancePerfect = 2 * (initialVelocity * T0 + 0.5 * maxAcceleration * T0 * T0);

        if (finalPosition - initialPosition <= distancePerfect) {
            // Time for one leg of triangular case
            double T1 = (-initialVelocity + Math.sqrt(initialVelocity * initialVelocity + maxAcceleration * (finalPosition - initialPosition))) / maxAcceleration;

            if (time >= 0 && time < T1) {
                return initialVelocity + maxAcceleration * time;
            } else if (time > T1 && time < 2 * T1) {
                return ((initialVelocity + maxAcceleration * T1) - maxAcceleration * (time - T1));
            } else if (time < 0) {
                throw new RuntimeException("Negative time");
            } else {
                throw new RuntimeException("Already completed");
            }
        } else {
            double T2 = (maxVelocity - finalVelocity) / maxAcceleration;
            double P1 = initialPosition + (initialVelocity * T0) + ((maxAcceleration * Math.pow(T0, 2)) / 2);
            double P2 = Math.pow(T2, 2) * (maxAcceleration / 2);
            double middleDistance = finalPosition - P1 - P2;
            double T1 = (middleDistance / maxVelocity) + T0;
            if (time >= 0 && time < T0) {
                return initialVelocity + (maxAcceleration * time);
            } else if (time >= T0 && time < T1) {
                return maxVelocity;
            } else if (time >= T1 && time <= T1 + T2) {
                return maxVelocity - (maxAcceleration * (time - T1));
            } else if (time < 0) {
                throw new RuntimeException("Negative time");
            } else {
                throw new RuntimeException("Already completed");
            }
        }
    }

    public double getAcceleration() {
        double T0 = (maxVelocity - initialVelocity) / maxAcceleration;
        double T2 = (maxVelocity - finalVelocity) / maxAcceleration;
        double P1 = initialPosition + (initialVelocity * T0) + ((maxAcceleration * Math.pow(T0, 2)) / 2);
        double P2 = Math.pow(T2, 2) * (maxAcceleration / 2);
        double middleDistance = finalPosition - P1 - P2;
        double T1 = (middleDistance / maxVelocity) + T0;
        if (time >= 0 && time < T0) {
            return maxAcceleration;
        } else if (time >= T0 && time < T1) {
            return 0;
        } else if (time >= T1 && time <= T1 + T2) {
            return -maxAcceleration;
        } else if (time < 0) {
            throw new RuntimeException("Negative time");
        } else {
            throw new RuntimeException("Already completed");
        }
    }

    public double getPosition() {
        double T0 = (maxVelocity - initialVelocity) / maxAcceleration;
        double T2 = (maxVelocity - finalVelocity) / maxAcceleration;
        double P1 = initialPosition + (initialVelocity * T0) + ((maxAcceleration * Math.pow(T0, 2)) / 2);
        double P2 = Math.pow(T2, 2) * (maxAcceleration / 2);
        double middleDistance = finalPosition - P1 - P2;
        double T1 = (middleDistance / maxVelocity) + T0;
        if (time >= 0 && time < T0) {
            return initialPosition + (initialVelocity * time) + ((maxAcceleration * Math.pow(time, 2)) / 2);
        } else if (time >= T0 && time < T1) {
            return P1 + maxVelocity * (time - T0);
        } else if (time >= T1 && time <= T1 + T2) {
            return (finalPosition - P2) + (maxVelocity * (time - T1)) - ((maxAcceleration * Math.pow(time - T1, 2)) / 2);
        } else if (time < 0) {
            throw new RuntimeException("Negative time");
        } else {
            throw new RuntimeException("Already completed");
        }
    }
}
