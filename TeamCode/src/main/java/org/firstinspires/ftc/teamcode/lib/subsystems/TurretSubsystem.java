/**
 * hi
 * We want to have multiple colour sensors to detect pollen/nectar (and whether we have accidentally picked up opposition piece [and if so, eject it]).
 * We also want to have a turret that can aim and shoot the pollen/nectar into the correct goal.
 * Turret: 2 methods:
 *     set aim direction
 *     set shooting (true/false) - should intelligently handle different game pieces / colours≥
*/

package org.firstinspires.ftc.teamcode.lib.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class TurretSubsystem implements Subsystem {
    private static final int CALIBRATION_CLOCKWISE_INITIAL_VALUE = 0;
    private static final int CALIBRATION_ANTICLOCKWISE_INITIAL_VALUE = 0;
    private static final int ENCODER_GEAR_RATIO = 8192;

    private final NormalizedColorSensor colorSensorLeft;
    private final NormalizedColorSensor colorSensorRight;
    private final DcMotor ShootyMotor;
    private final Servo RotatorServo;
    private final DcMotor aimEncoder;
    private final DigitalChannel halleffectSensor;
    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;
    private final Telemetry telemetry;
    private int encoderZero = 0;
    private int previousEncoderPosition = 0;
    private boolean previousHallEffectState = false;
    private boolean autoEnabled = false;
    private double manualAimAngle = 0;

    public TurretSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "turretcolorSensorLeft");
        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "turretcolorSensorRight");
        ShootyMotor = hardwareMap.get(DcMotor.class, "turretShootyMotor");
        RotatorServo = hardwareMap.get(Servo.class, "turretRotatorServo");
        aimEncoder = hardwareMap.get(DcMotor.class, "turretThoroghboreEncoder_notMotor");
        halleffectSensor = hardwareMap.get(DigitalChannel.class, "turrethalleffectSensor");
        initAprilTag();
        this.telemetry = telemetry;
    }

    private void initAprilTag() {
        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();

        // Create the vision portal the easy way.
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Turret camera"), aprilTag);


    }

    private int getEncoderPosition() {
        return aimEncoder.getCurrentPosition() - encoderZero;
    }

    private double getAimAngle() {
        double revolution = getEncoderPosition() / (double) ENCODER_GEAR_RATIO;
        return revolution * 2 * Math.PI;
    }

    public void setAuto(boolean enabled) {
        autoEnabled = enabled;
    }

    public void setManualAngle(double angle) {
        manualAimAngle = angle;
    }

    public void shoot() {
        ShootyMotor.setPower(1);
    }

    public void off() {
        ShootyMotor.setPower(0);
    }

    public void calibrate() {
        // get the rotation direction of the turret
        int currentEncoderPosition = aimEncoder.getCurrentPosition();
        if (currentEncoderPosition == previousEncoderPosition) return;
        boolean rotatingClockwise = currentEncoderPosition > previousEncoderPosition;

        // check if the hall effect sensor was just triggered (rising edge)
        boolean currentHallEffectState = halleffectSensor.getState();
        boolean hallEffectJustTriggered = !previousHallEffectState && currentHallEffectState;

        if (hallEffectJustTriggered) {
            if (rotatingClockwise) {
                encoderZero = currentEncoderPosition + CALIBRATION_CLOCKWISE_INITIAL_VALUE;
            } else {
                encoderZero = currentEncoderPosition + CALIBRATION_ANTICLOCKWISE_INITIAL_VALUE;
            }
        }

        previousEncoderPosition = currentEncoderPosition;
        previousHallEffectState = currentHallEffectState;
    }

    private void manualAim() {
        // pid on the aimAngle

        double currentAngle = getAimAngle();
        double error = manualAimAngle - currentAngle;
        double output = 0.5 + 0.0001 * error;
        RotatorServo.setPosition(output);
    }

    private void autoAim() {

        // if we don't see an april, we can't do anything
        //  -> defer to manual control

        // otherwise, we do see an apriltag
        // goal: center the apriltag in the camera's vision

        // -> pid on the apriltag position in the camera

        // current value -> x coordinate of the apriltag's center
        // target -> halfway in the center of the image
        // -> error -> pid calculations -> yippee

        // TODO implement this
    }
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;

                double x = clusterDet.ftcPose.x;
                double bearing = clusterDet.ftcPose.bearing;
            }
        }

        // TODO finish this
    }
    @Override
    public void periodic() {
        // if !autoEnabled -> use the manual angle PID
        if (autoEnabled) {
            autoAim();
        } else {
            manualAim();
        }
    }

}



