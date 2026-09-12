package org.firstinspires.ftc.teamcode.lib;

public class Utils {
    /**
     * Returns the relative angle between two angles, accounting for continuous rotation
     *
     * @param a The first angle, in the range [0, 360]
     * @param b The second angle, in the range [0, 360]
     * @return The wrapped signed angle between a and b
     */
    public static double wrappedSignedAngleBetween(double a, double b) {
        double result = a - b;
        if (result < -Math.PI) result += 2 * Math.PI;
        else if (result > Math.PI) result -= 2 * Math.PI;
        return result;
    }
}
