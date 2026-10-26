package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeSubsystem implements Subsystem {
    private final DcMotor spinMotorLeft;
    private final DcMotor spinMotorRight;
    private final Servo deployServoLeft;
    private final Servo deployServoRight;
    private final Servo starServoLeft;
    private final Servo starServoRight;
    private final DigitalChannel deployLimitSwitchTop;
    private final DigitalChannel deployLimitSwitchBottom;
    private final Telemetry telemetry;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        spinMotorLeft = hardwareMap.get(DcMotor.class, "intakeSpinMotorLeft");
        spinMotorRight = hardwareMap.get(DcMotor.class, "intakeSpinMotorRight");
        deployServoLeft = hardwareMap.get(Servo.class, "intakeDeployServoLeft");
        deployServoRight = hardwareMap.get(Servo.class, "intakeDeployServoRight");
        starServoLeft = hardwareMap.get(Servo.class, "intakeStarServoLeft");
        starServoRight = hardwareMap.get(Servo.class, "intakeStarServoRight");
        deployLimitSwitchTop = hardwareMap.get(DigitalChannel.class, "intakeDeployLimitSwitchTop");
        deployLimitSwitchBottom = hardwareMap.get(DigitalChannel.class, "intakeDeployLimitSwitchBottom");
        this.telemetry = telemetry;
    }

    /**
     * Intakes game pieces from the flower. Raises the intake bar, and spins the intake stars.
     */
    public void setFlower() {
        spinMotorLeft.setPower(1);
        spinMotorRight.setPower(1);
        starServoLeft.setPosition(1);
        starServoRight.setPosition(1);
        if (!limitSwitchTriggered()) {
            deployServoLeft.setPosition(0);
            deployServoRight.setPosition(0);
        }
    }

    /**
     * Intakes game pieces from the ground. Lowers the intake bar, and spins the intake stars.
     */
    public void setGround() {
        spinMotorLeft.setPower(1);
        spinMotorRight.setPower(1);
        starServoLeft.setPosition(0);
        starServoRight.setPosition(0);
        if (!limitSwitchTriggered()) {
            deployServoLeft.setPosition(1);
            deployServoRight.setPosition(1);
        }
    }

    /**
     * Turns off intake motors/servos associated with the Intake.
     */
    public void off() {
        spinMotorLeft.setPower(0);
        spinMotorRight.setPower(0);
        starServoLeft.setPosition(0.5);
        starServoRight.setPosition(0.5);
        deployServoLeft.setPosition(0.5);
        deployServoRight.setPosition(0.5);
    }

    /**
     * @return true if either deployment limit switch is triggered
     */
    private boolean limitSwitchTriggered() {
        return deployLimitSwitchTop.getState() || deployLimitSwitchBottom.getState();
    }

    @Override
    public void periodic() {
        if (limitSwitchTriggered()) {
            deployServoLeft.setPosition(0.5);
            deployServoRight.setPosition(0.5);
        }

        telemetry.addData("Intake Deploy Limit Switch Top State", deployLimitSwitchTop.getState());
        telemetry.addData("Intake Deploy Limit Switch Bottom State", deployLimitSwitchBottom.getState());
    }
}
