package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Intake Deploy Tuning")
public class IntakeDeployTuningOpMode extends OpMode {
    Servo deployServoLeft;
    Servo deployServoRight;

    double leftPosition = 0.5;
    double rightPosition = 0.5;

    public void init() {
        deployServoLeft = hardwareMap.get(Servo.class, "intakeDeployServoLeft");
        deployServoRight = hardwareMap.get(Servo.class, "intakeDeployServoRight");
    }

    public void loop() {
        if (gamepad1.dpadUpWasPressed()) {
            leftPosition += 0.05;
        } else if (gamepad1.dpadDownWasPressed()) {
            leftPosition -= 0.05;
        }

        if (gamepad1.yWasPressed()) {
            rightPosition += 0.05;
        } else if (gamepad1.bWasPressed()) {
            rightPosition -= 0.05;
        }

        deployServoLeft.setPosition(leftPosition);
        deployServoRight.setPosition(rightPosition);

        telemetry.addData("Left Position", leftPosition);
        telemetry.addData("Right Position", rightPosition);
        telemetry.update();
    }
}
