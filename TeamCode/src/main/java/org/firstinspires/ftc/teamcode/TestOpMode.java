package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Test")
public class TestOpMode extends OpMode {
    Servo deployServoLeft;
    Servo deployServoRight;
    DcMotor spinMotorLeft;
    DcMotor spinMotorRight;

    public void init() {
        deployServoLeft = hardwareMap.get(Servo.class, "intakeDeployServoLeft");
        deployServoRight = hardwareMap.get(Servo.class, "intakeDeployServoRight");
        spinMotorLeft = hardwareMap.get(DcMotor.class, "intakeSpinMotorLeft");
        spinMotorRight = hardwareMap.get(DcMotor.class, "intakeSpinMotorRight");
        spinMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        spinMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
        if (gamepad1.dpad_up) {
            deployServoLeft.setPosition(0);
        } else if (gamepad1.dpad_down) {
            deployServoLeft.setPosition(1);
        } else {
            deployServoLeft.setPosition(0.5);
        }

        if (gamepad1.y) {
            deployServoRight.setPosition(0);
        } else if (gamepad1.a) {
            deployServoRight.setPosition(1);
        } else {
            deployServoRight.setPosition(0.5);
        }

        if (gamepad1.left_trigger_pressed) {
            spinMotorLeft.setPower(1);
            spinMotorRight.setPower(1);
        } else {
            spinMotorLeft.setPower(0);
            spinMotorRight.setPower(0);
        }
    }
}
