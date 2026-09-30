/* hi
this is the intake subsystem.
the intake subsystems role is to intake game pieces, using the mecanum roller and horizontal roller wheels.
It will also be able to raise and lower the mecanum roller to allow for different intake angles.
this will feature methods that control the intake system, which will consist of
three methods:
 - set speed of mecanum roller
 - set speed of horizontal roller wheels
 - raise/lower mecanum roller



 */

package org.firstinspires.ftc.teamcode.lib.subsystems;

public class IntakeSubsystem implements Subsystem {
    private DcMotor Intake_motor;
    private DcMotor arm_motor;//might be needed
    public void activate_intake(){
        float throttle = gamepad1.left_stick_y;
        boolean y_button = gamepad1.y;
        if (y_button){
            shooter.setPower(1);
        }
        else{
            shooter.setPower(0);
        }
    }
}
    public void init(){
        Intake_motor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");


    }


