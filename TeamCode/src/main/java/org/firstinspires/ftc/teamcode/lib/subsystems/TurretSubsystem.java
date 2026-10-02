/* hi
We want to have multiple colour sensors to detect pollen/nectar (and whether we have accidentally picked up opposition piece [and if so, eject it]).
We also want to have a turret that can aim and shoot the pollen/nectar into the correct goal.
Turret: 2 methods:
    set aim direction
    set shooting (true/false) - should intelligently handle different game pieces / colours≥
*/

package org.firstinspires.ftc.teamcode.lib.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TurretSubsystem implements Subsystem {
    private DcMotor shootermotor;
    private DcMotor turretmotor;
    private DcMotor indexing;

    //Function that determine the which color hive should be lock on
    /*
    public void location_targeting (String Team_color){
        if (Team_color.equals("Blue")){

        }
        if(Team_color.equals("Red")){

        }
        //Fill with method so that the robot will be able to adjust the turret to lock onto both hives
    }*/
    public void Shoot() {

        boolean x_button = gamepad1.x;
        if (x_button) {
            shootermotor.setPower(1);
            indexing.setPower(1):
        } else {
            shootermotor.setPower(0);
            indexing.setPower(0):
        }
    }
    public void rotate_turret(){
        boolean dpad_left = gamepad1.dpad_left;
        boolean dpad_right = gamepad1.dpad_right;
        if (dpad_left){
            turretmotor.setPower(0.5);
        }
        if (dpad_right){
            turretmotor.setPower(-0.5);
        }
    }


    public void init(){
        //Motor setup please remeber get the name of the motor from control hub
        shootermotor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        turretmotor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        indexing = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        turretmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //This part still deciding on how to get the turret to lock on to the hives




    }
    public void loop(){


    }
}
