
package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem implements Subsystem {
    private final DcMotor spinMotorleft;
    private final DcMotor spinMotorright;
    private final Servo updownBarleft;
    private final Servo updownBarright;
    private final Servo spinStarleft;
    private final Servo spinStarright;
    private final DigitalChannel updownBarlimitSwitch;
    private final Telemetry telemetry;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        spinMotorleft = hardwareMap.get(DcMotor.class, "IntakespinMotorleft");
        spinMotorright = hardwareMap.get(DcMotor.class, "IntakespinMotorright");
        updownBarleft = hardwareMap.get(Servo.class, "IntakeupdownBarleft");
        updownBarright = hardwareMap.get(Servo.class, "IntakeupdownBarright");
        spinStarleft = hardwareMap.get(Servo.class, "IntakespinStarleft");
        spinStarright = hardwareMap.get(Servo.class, "IntakespinStarright");
        updownBarlimitSwitch = hardwareMap.get(DigitalChannel.class, "IntakeupdownBarlimitSwitch");
        this.telemetry = telemetry;
    }

    public void intakeFlower() {
        spinMotorleft.setPower(1);
        spinMotorright.setPower(1);
        spinStarleft.setPosition(1);
        spinStarright.setPosition(1);
        updownBarleft.setPosition(0);
        updownBarright.setPosition(0);
    }

    public void intakeGround() {
        spinMotorleft.setPower(1);
        spinMotorright.setPower(1);
        spinStarleft.setPosition(0);
        spinStarright.setPosition(0);
        updownBarleft.setPosition(1);
        updownBarright.setPosition(1);
    }

    public void intakeOff() {
        spinMotorleft.setPower(0);
        spinMotorright.setPower(0);
        spinStarleft.setPosition(0.5);
        spinStarright.setPosition(0.5);
        updownBarleft.setPosition(0.5);
        updownBarright.setPosition(0.5);
    }
}
