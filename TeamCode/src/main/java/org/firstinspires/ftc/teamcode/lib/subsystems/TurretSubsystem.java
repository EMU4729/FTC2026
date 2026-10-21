/**
 * hi
 * We want to have multiple colour sensors to detect pollen/nectar (and whether we have accidentally picked up opposition piece [and if so, eject it]).
 * We also want to have a turret that can aim and shoot the pollen/nectar into the correct goal.
 * Turret: 2 methods:
 *     set aim direction
 *     set shooting (true/false) - should intelligently handle different game pieces / colours≥
*/

package org.firstinspires.ftc.teamcode.lib.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TurretSubsystem implements Subsystem {
    private DcMotor shootermotor;
    private Servo turretservo;
    private DcMotor encoder;
    private int gear_ratio = 8192;
    static final double MAX_POS =  180.0;     // Maximum rotational position
    static final double MIN_POS= 0.0;

    double position = (MAX_POS - MIN_POS) / 2; // Start at halfway position

    // TODO: define Servo for turret turning
    // TODO: define encoder for measuring turret turning (see examples for how to define)

    public TurretSubsystem(HardwareMap hardwareMap){
        //Motor setup please remeber get the name of the motor from control hub
        shootermotor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        turretservo = hardwareMap.get(Servo.class,"PLACE HOLDER" );
        encoder = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        encoder.getCurrentPosition();

        // TODO setup devices
        //This part still deciding on how to get the turret to lock on to the hives
    }


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
        // make the shooter motor shoot (setPower call)
        // that's it

        boolean x_button = gamepad1.x;
        if (x_button) {
            shootermotor.setPower(1);

        } else {
            shootermotor.setPower(0);

        }
    }

    // TODO should accept a double representing "how much" / "how fast" to turn
    public void rotate_turret(int degree){
        //TODO: Do math of turret to turn to specific angles



        // TODO should make the turning Servo move
        // safety check to prevent rotation over limit of the turret
        int target_degree  = degree;
        if (target_degree >= MAX_POS ) {
            target_degree = MAX_POS;
            // Switch ramp direction
        }

        if (target_degree <= MIN_POS ) {
            target_degree = MIN_POS;
        }
        // Set motor
        double cur_position = encoder.getCurrentPosition();
        double revolution = cur_position/gear_ratio;
        double rads = revolution * Math.PI();
        double angle_error = (target_degree - cur_position)+0.5 ;
        double rads = revolution * Math.PI;
        if(position<){
            int power
        }

        turretservo.setPosition(angle_error *0.01);

    }

    // TODO off method - just stops all movement
    public Offmode () {

}
    public void loop(){


    }
}
