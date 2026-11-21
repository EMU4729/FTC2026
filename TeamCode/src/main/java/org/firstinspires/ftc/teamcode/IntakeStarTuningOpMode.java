package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Intake Star Tuning")
public class IntakeStarTuningOpMode extends OpMode {
    Servo deployServoLeft;
    Servo deployServoRight;

    public void init() {
        deployServoLeft = hardwareMap.get(Servo.class, "intakeStarServoLeft");
        deployServoRight = hardwareMap.get(Servo.class, "intakeStarServoRight");
    }

    public void loop() {
        if (gamepad1.dpad_right) {
            deployServoLeft.setPosition(1);
        } else if (gamepad1.dpad_left) {
            deployServoLeft.setPosition(0);
        } else {
            deployServoLeft.setPosition(0.5);
        }

        if (gamepad1.b) {
            deployServoRight.setPosition(1);
        } else if (gamepad1.x) {
            deployServoRight.setPosition(0);
        } else {
            deployServoRight.setPosition(0.5);
        }
    }
}
