package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Globals;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class TurretSubsystem implements Subsystem {
    private static final int CALIBRATION_CLOCKWISE_INITIAL_VALUE = 0; // TODO tune this
    private static final int CALIBRATION_ANTICLOCKWISE_INITIAL_VALUE = 0; // TODO tune this
    private static final double AIM_LOWER_LIMIT = -Math.PI / 2; // TODO tune this
    private static final double AIM_UPPER_LIMIT = Math.PI / 2; // TODO tune this
    private static final int ENCODER_GEAR_RATIO = 8192;
    private static final double SHOOT_PID_P = 1;
    private static final double SHOOT_PID_I = 0;
    private static final double SHOOT_PID_D = 0;
    private static final double SHOOT_PID_F = 0;

    //    private final NormalizedColorSensor colorSensorLeft;
//    private final NormalizedColorSensor colorSensorRight;
    private final DcMotorEx shootMotorPrimary;
    private final DcMotorEx shootMotorSecondary;
    private final Servo aimServo;
    private final DcMotor aimEncoder;
    //    private final DigitalChannel hallEffectSensor;
    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;
    private final Telemetry telemetry;
    private final boolean isRedAlliance;
    private int previousEncoderPosition = 0;
    private boolean previousHallEffectState = false;
    private boolean autoEnabled = false;

    public TurretSubsystem(HardwareMap hardwareMap, Telemetry telemetry, boolean isRedAlliance) {
//        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "turretColorSensorLeft");
//        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "turretColorSensorRight");
        shootMotorPrimary = hardwareMap.get(DcMotorEx.class, "turretShootPrimaryMotor");
        shootMotorSecondary = hardwareMap.get(DcMotorEx.class, "turretShootSecondaryMotor");
        aimServo = hardwareMap.get(Servo.class, "turretAimServo");
        aimEncoder = hardwareMap.get(DcMotor.class, "turretAimEncoder");
//        hallEffectSensor = hardwareMap.get(DigitalChannel.class, "turretHallEffectSensor");
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "turretCamera"), aprilTag);

        aimServo.setDirection(Servo.Direction.FORWARD);

        shootMotorPrimary.setVelocityPIDFCoefficients(SHOOT_PID_P, SHOOT_PID_I, SHOOT_PID_D, SHOOT_PID_F);
        shootMotorSecondary.setVelocityPIDFCoefficients(SHOOT_PID_P, SHOOT_PID_I, SHOOT_PID_D, SHOOT_PID_F);
        shootMotorPrimary.setDirection(DcMotorSimple.Direction.FORWARD);
        shootMotorSecondary.setDirection(DcMotorSimple.Direction.FORWARD);

        this.telemetry = telemetry;
        this.isRedAlliance = isRedAlliance;
    }

    /**
     * Sets the power of the aiming servo, taking into account software-defined rotation limits.
     *
     * @param power The desired power, in the range [-1, 1].
     */
    private void setAimServoPower(double power) {
        double aimAngle = getAimAngle();
        if ((power < 0 && aimAngle < AIM_LOWER_LIMIT) || (power > 0 && aimAngle > AIM_UPPER_LIMIT)) {
            aimServo.setPosition(0.5);
            return;
        }

        // normalize [-1, 1] power to [0, 1] accepted by setPosition
        double out = Math.clamp(0.5 + power / 2, -1, 1);
        aimServo.setPosition(out);

    }

    /**
     * @return The current aim encoder position (in ticks) relative to the calibrated encoderZero.
     */
    private int getEncoderPosition() {
        return aimEncoder.getCurrentPosition() - Globals.turretEncoderZero;
    }

    /**
     * Converts between aim encoder ticks and radians.
     *
     * @param ticks Number of ticks
     * @return Number of radians for given ticks
     */
    private double ticksToRadians(int ticks) {
        return (ticks / (double) ENCODER_GEAR_RATIO) * 2 * Math.PI;
    }

    /**
     * @return The current aiming angle (in radians) relative to the calibrated encoderZero.
     */
    private double getAimAngle() {
        return ticksToRadians(getEncoderPosition());
    }

    /**
     * Sets whether the subsystem should use auto aim or manual.
     *
     * @param enabled true for auto aim, false for manual.
     */
    public void setAuto(boolean enabled) {
        autoEnabled = enabled;
    }

    /**
     * Sets the power of the turret aim servo in manual mode. Note: Has no effect if the turret is using auto aim, see {@link setAuto}.
     *
     * @param power The power, in the range [-1, 1].
     */
    public void setManualPower(double power) {
        if (!autoEnabled) {
            setAimServoPower(power);
        }
    }

    /**
     * Turns on the shooting motor.
     *
     * @param speed Desired shooting speed, in ticks/s. (With the shooter's effective 1:1 gear ratio, this is effectively equivalent to rot/s)
     */
    public void shoot(double speed) {
        shootMotorPrimary.setVelocity(speed);
        shootMotorSecondary.setVelocity(speed);
    }

    public void dump() {
        shootMotorPrimary.setPower(0.25);
        shootMotorSecondary.setPower(0.25);
    }

    /**
     * Turns off the shooting motor.
     */
    public void off() {
        shootMotorPrimary.setPower(0);
        shootMotorSecondary.setPower(0);
    }

    /**
     * Calibrates the zero position of the turret aim encoder using the hall effect sensor.
     * Should be called repeatedly while calibration is active, probably during `init_loop`.
     *
     * @return whether the turret was calibrated or not.
     */
    public boolean calibrate() {
        return true;

        // get the rotation direction of the turret
//        int currentEncoderPosition = aimEncoder.getCurrentPosition();
//        if (currentEncoderPosition == previousEncoderPosition) return false;
//        boolean rotatingClockwise = currentEncoderPosition > previousEncoderPosition;
//
//        // check if the hall effect sensor was just triggered (rising edge)
//        boolean currentHallEffectState = hallEffectSensor.getState();
//        boolean hallEffectJustTriggered = !previousHallEffectState && currentHallEffectState;
//
//        if (hallEffectJustTriggered) {
//            if (rotatingClockwise) {
//                Globals.turretEncoderZero = currentEncoderPosition + CALIBRATION_CLOCKWISE_INITIAL_VALUE;
//            } else {
//                Globals.turretEncoderZero = currentEncoderPosition + CALIBRATION_ANTICLOCKWISE_INITIAL_VALUE;
//            }
//            Globals.turretIsCalibrated = true;
//        }
//
//        previousEncoderPosition = currentEncoderPosition;
//        previousHallEffectState = currentHallEffectState;
//        return hallEffectJustTriggered;
    }

    /**
     * Periodic code for auto aiming.
     */
    private void autoAim() {
        List<AprilTagDetection> detections = aprilTag.getDetections();

        telemetry.addData("Turret # AprilTags Detections", detections.size());

        AprilTagDetection targetDetection = null;
        for (AprilTagDetection detection : detections) {
            if (!(detection instanceof AprilTagClusterDetection)) continue;
            AprilTagClusterDetection cluster = (AprilTagClusterDetection) detection;

            // same approach as in https://ftc-docs.firstinspires.org/en/latest/tech_tips/tech-tips/tech-tip-apriltag-clusters/tech-tip-apriltag-clusters.html#determining-if-a-cluster-should-be-targeted
            boolean tagIsRedAlliance = cluster.metadata.name.startsWith("R");
            boolean isFacingUp = Math.abs(cluster.ftcPose.roll) < 90;
            if (tagIsRedAlliance != isRedAlliance || !isFacingUp) continue;

            targetDetection = detection;
            break;
        }

        // if we don't see an apriltag, we can't do anything
        // TODO: defer to... something else in this case
        if (targetDetection == null) return;

        // otherwise, PID on the target X and heading (we want both to be 0)
        telemetry.addData("Turret Auto Aim Target Bearing", targetDetection.ftcPose.bearing);
        double out = targetDetection.ftcPose.bearing;
        setAimServoPower(out);
    }

    @Override
    public void periodic() {
        if (autoEnabled) {
            autoAim();
        }

//        telemetry.addData("Turret Hall Effect Sensor State", hallEffectSensor.getState());
        telemetry.addData("Turret Calibrated?", Globals.turretIsCalibrated);
        telemetry.addData("Turret Aim Position (ticks, raw)", aimEncoder.getCurrentPosition());
        telemetry.addData("Turret Aim Position (ticks, zeroed)", getEncoderPosition());
        telemetry.addData("Turret Aim Zero (ticks)", Globals.turretEncoderZero);
        telemetry.addData("Turret Aim Zero (rad)", ticksToRadians(Globals.turretEncoderZero));
        telemetry.addData("Turret Aim Position (rad, zeroed)", getAimAngle());
    }
}
