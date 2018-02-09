package org.team114.ocelot.util.motion;

public class MotionProfile {
//
//    private final double time, initialPosition, finalPosition, initialVelocity, finalVelocity;
//
//    // Variables that are calculated based on arguments that are to be used by methods to calculate velocity,
//    // position, and acceleration for a given time
//    private double distancePerfect, middleDistance, T0, T1, T2, P1, P2;
//    private final boolean triangular, completed;
//    private double timePeriodOne, timePeriodTwo, timePeriodThree;
//
//    public MotionProfile(double time, double initialPosition, double finalPosition,
//                         double initialVelocity, double finalVelocity) {
//        this.time = time;
//        this.initialPosition = initialPosition;
//        this.finalPosition = finalPosition;
//        this.initialVelocity = initialVelocity;
//        this.finalVelocity = finalVelocity;
//
//        distancePerfect = 2 * (initialVelocity * T0 + 0.5 * Settings.MAX_ACCELERATION * T0 * T0);
//        T0 = (Settings.MAX_VELOCITY - initialVelocity) / Settings.MAX_ACCELERATION;
//
//        if (finalPosition - initialPosition <= distancePerfect) {
//            triangular = true;
//
//            T1 = (-initialVelocity + Math.sqrt(initialVelocity * initialVelocity +
//                    Settings.MAX_ACCELERATION * (finalPosition - initialPosition))) /
//                    Settings.MAX_ACCELERATION;
//
//            timePeriodOne = T1;
//            timePeriodTwo = 2 * T1;
//        } else {
//            triangular = false;
//
//            T2 = (Settings.MAX_VELOCITY - finalVelocity) / Settings.MAX_ACCELERATION;
//            P1 = initialPosition + (initialVelocity * T0) + ((Settings.MAX_ACCELERATION *
//                    Math.pow(T0, 2)) / 2);
//
//            P2 = Math.pow(T2, 2) * (Settings.MAX_ACCELERATION / 2);
//            middleDistance = finalPosition - P1 - P2;
//            T1 = (middleDistance / Settings.MAX_VELOCITY) + T0;
//
//            timePeriodOne = T0;
//            timePeriodTwo = T1;
//            timePeriodThree = T1 + T2;
//        }
//
//        // Error handling
//        if (time < 0) {
//            throw new IllegalArgumentException("Negative time");
//        }
//
//        completed = (triangular && time > timePeriodTwo) || (!triangular && time > timePeriodThree);
//    }
//
//    public boolean isCompleted() {
//        return completed;
//    }
//
//    public double getVelocity() {
//        if (triangular) {
//            if (time >= 0 && time < timePeriodOne) {
//                return initialVelocity + Settings.MAX_ACCELERATION * time;
//            } else if (time >= timePeriodOne && time < timePeriodTwo) {
//                return ((initialVelocity + Settings.MAX_ACCELERATION * T1) -
//                        Settings.MAX_ACCELERATION * (time - T1));
//            }
//        } else {
//            if (time >= 0 && time < timePeriodOne) {
//                return initialVelocity + (Settings.MAX_ACCELERATION * time);
//            } else if (time >= timePeriodOne && time < timePeriodTwo) {
//                return Settings.MAX_VELOCITY;
//            } else if (time >= timePeriodTwo && time <= timePeriodThree) {
//                return Settings.MAX_VELOCITY - (Settings.MAX_ACCELERATION * (time - T1));
//            }
//        }
//        if (completed) {
//            return finalVelocity;
//        }
//        throw new AssertionError("End of method should be unreachable");
//    }
//
//    public double getAcceleration() {
//        if (time >= 0 && time < timePeriodOne) {
//            return Settings.MAX_ACCELERATION;
//        } else if (time >= timePeriodOne && time < timePeriodTwo) {
//            return 0;
//        } else if (time >= timePeriodTwo && time <= timePeriodThree) {
//            return -Settings.MAX_ACCELERATION;
//        } else if (completed) {
//            return 0;
//        }
//        throw new AssertionError("End of method should be unreachable");
//    }
//
//    public double getPosition() {
//        if (time >= 0 && time < timePeriodOne) {
//            return initialPosition + (initialVelocity * time) + ((Settings.MAX_ACCELERATION *
//                    Math.pow(time, 2)) / 2);
//        } else if (time >= timePeriodOne && time < timePeriodTwo) {
//            return P1 + Settings.MAX_VELOCITY * (time - T0);
//        } else if (time >= timePeriodTwo && time <= timePeriodThree) {
//            return (finalPosition - P2) + (Settings.MAX_VELOCITY * (time - T1)) -
//                    ((Settings.MAX_ACCELERATION * Math.pow(time - T1, 2)) / 2);
//        } else if (completed) {
//            return finalPosition;
//        }
//
//        throw new AssertionError("End of method should be unreachable");
//    }
}
