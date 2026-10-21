/**
 * hi
 * this is the intake subsystem.
 * the intake subsystems role is to intake game pieces, using the mecanum roller and horizontal roller wheels.
 * It will also be able to raise and lower the mecanum roller to allow for different intake angles.
 * this will feature methods that control the intake system, which will consist of
 * three methods:
 * - set speed of mecanum roller
 * - set speed of horizontal roller wheels
 * - raise/lower mecanum roller
 */

package org.firstinspires.ftc.teamcode.lib.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IntakeSubsystem implements Subsystem {
    private DcMotor Intake_motor;
    private DcMotor arm_motor;//might be needed

    public IntakeSubsystem(HardwareMap hardwareMap){
        Intake_motor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
    }

    public void activate_intake(){
        boolean y_button = gamepad1.y;
        if (y_button){
            Intake_motor.setPower(1);
        }
        else{
            Intake_motor.setPower(0);
        }
    }

}


