/**
 * hi
 * We want to have multiple colour sensors to detect pollen/nectar (and whether we have accidentally picked up opposition piece [and if so, eject it]).
 * We also want to have a turret that can aim and shoot the pollen/nectar into the correct goal.
 * Turret: 2 methods:
 *     set aim direction
 *     set shooting (true/false) - should intelligently handle different game pieces / colours≥
*/

package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TurretSubsystem implements Subsystem {
    private final NormalizedColorSensor colorSensorLeft;
    private final NormalizedColorSensor colorSensorRight;
    private final DcMotor ShootyMotor;
    private final Servo RotatorServo;
    private final DcMotor ThoroghboreEncoder_notMotor;
    private final DigitalChannel halleffectSensor;
    private final Telemetry telemetry;



    public TurretSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "turretcolorSensorLeft");
        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "turretcolorSensorRight");
        ShootyMotor = hardwareMap.get(DcMotor.class, "turretShootyMotor");
        RotatorServo = hardwareMap.get(Servo.class, "turretRotatorServo");
        ThoroghboreEncoder_notMotor = hardwareMap.get(DcMotor.class, "turretThoroghboreEncoder_notMotor");
        halleffectSensor = hardwareMap.get(DigitalChannel.class, "turrethalleffectSensor");
        this.telemetry = telemetry;

    }

    public void shoot() {
        ShootyMotor.setPower(1);
    }
    public void off() {
        ShootyMotor.setPower(0);
    }

    public void calibrate() {
        // get the rotation direction of the turret
        // check if the hall effect sensor is triggered
        // if hall effect sensor is triggered:
        // - and we are going anticlockwise, set the current position to something
        // - and we are going clockwise, set the current position to something else.
    }




}



