package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController {
    private final double kp;
    private final double ki;
    private final double kd;
    private final double f;
    private final ElapsedTime timer = new ElapsedTime();

    private double prevTime = timer.time();
    private double prevError = 0;
    private double integral = 0;
    private double izone = Double.POSITIVE_INFINITY;

    public PIDController(double p, double i, double d, double f) {
        this.kp = p;
        this.ki = i;
        this.kd = d;
        this.f = f;
    }

    public PIDController withIZone(double izone) {
        this.izone = izone;
        return this;
    }

    public void resetIntegral() {
        integral = 0;
    }

    public double calculate(double error) {
        double dt = timer.time() - prevTime;

        if (Math.abs(error) > izone) {
            resetIntegral();
        } else if (ki != 0) {
            integral += error * dt;
        }
        double derivative = (error - prevError) / dt;

        double result = kp * error + ki * integral + kd * derivative + f;

        prevError = error;
        return result;
    }
}
