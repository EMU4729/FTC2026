/* hi
We want to have multiple colour sensors to detect pollen/nectar (and whether we have accidentally picked up opposition piece [and if so, eject it]).
We also want to have a turret that can aim and shoot the pollen/nectar into the correct goal.
Turret: 2 methods:
    set aim direction
    set shooting (true/false) - should intelligently handle different game pieces / colours≥
*/

package org.firstinspires.ftc.teamcode.lib.subsystems;

public class TurretSubsystem implements Subsystem {
    private DcMotor shootermotor;
    private DcMotor turretmotor;
    private DcMotor indexing;
    private int[] Bluehive1_cordinates;
    private int[] Bluehive2_cordinates;
    private int[] Redhive1_cordinates;
    private int[] Redhive2_cordinates;
    //Function that determine the which color hive should be lock on
    public void location_targeting (String Team_color){
        if (Team_color.equals("Blue")){

        }
        if(Team_color.equals("Red")){

        }
        //Fill with method so that the robot will be able to adjust the turret to lock onto both hives
    }

    return
    public void init(){
        //Motor setup please remeber get the name of the motor from control hub
        shootermotor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        turretmotor = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        indexing = hardwareMap.get(DcMotor.class, "PLACE HOLDER");
        //This part still deciding on how to get the turret to lock on to the hives
        Bluehive1_cordinates = new int[]{};
        Bluehive2_cordinates= new int[]{};
        Redhive1_cordinates = new int[]{};
        Redhive2_cordinates = new int[]{};



    }
    public void loop(){


    }
}
