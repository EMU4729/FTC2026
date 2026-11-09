package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
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

    private final NormalizedColorSensor colorSensorLeft;
    private final NormalizedColorSensor colorSensorRight;
    private final DcMotor shootMotor;
    private final Servo aimServo;
    private final DcMotor aimEncoder;
    private final DigitalChannel hallEffectSensor;
    private AprilTagProcessor aprilTag;

    private VisionPortal visionPortal;
    private final Telemetry telemetry;
    private final boolean isRedAlliance;
    private int encoderZero = 0;
    private int previousEncoderPosition = 0;
    private boolean previousHallEffectState = false;
    private boolean autoEnabled = false;
    private double manualAimAngle = 0;

    public TurretSubsystem(HardwareMap hardwareMap, Telemetry telemetry, boolean isRedAlliance) {
        colorSensorLeft = hardwareMap.get(NormalizedColorSensor.class, "turretColorSensorLeft");
        colorSensorRight = hardwareMap.get(NormalizedColorSensor.class, "turretColorSensorRight");
        shootMotor = hardwareMap.get(DcMotor.class, "turretShootMotor");
        aimServo = hardwareMap.get(Servo.class, "turretAimServo");
        aimEncoder = hardwareMap.get(DcMotor.class, "turretAimEncoder");
        hallEffectSensor = hardwareMap.get(DigitalChannel.class, "turretHallEffectSensor");
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "turretCamera"), aprilTag);
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
        return aimEncoder.getCurrentPosition() - encoderZero;
    }

    /**
     * Converts between aim encoder ticks and radians.
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
     */
    public void shoot() {
        shootMotor.setPower(1);
    }
    public void dump() {
        shootMotor.setPower(0.25);
    }

    /**
     * Turns off the shooting motor.
     */
    public void off() {
        shootMotor.setPower(0);
    }

    /**
     * Calibrates the zero position of the turret aim encoder using the hall effect sensor.
     * Should be called repeatedly while calibration is active, probably during `init_loop`.
     */
    public void calibrate() {
        // get the rotation direction of the turret
        int currentEncoderPosition = aimEncoder.getCurrentPosition();
        if (currentEncoderPosition == previousEncoderPosition) return;
        boolean rotatingClockwise = currentEncoderPosition > previousEncoderPosition;

        // check if the hall effect sensor was just triggered (rising edge)
        boolean currentHallEffectState = hallEffectSensor.getState();
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

    /**
     * Periodic code for auto aiming.
     */
    private void autoAim() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        // TODO: add apriltag telemetry (important!!)

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
        // TODO: defer to manual control in this case
        if (targetDetection == null) return;

        // otherwise, PID on the target X and heading (we want both to be 0)
        // TODO: the below probably isn't a good way to implement this.
        double out = targetDetection.ftcPose.x + targetDetection.ftcPose.bearing;
        setAimServoPower(out);
    }

    @Override
    public void periodic() {
        if (autoEnabled) {
            autoAim();
        }

        telemetry.addData("Turret Hall Effect Sensor State", hallEffectSensor.getState());
        telemetry.addData("Turret Aim Position (ticks, raw)", aimEncoder.getCurrentPosition());
        telemetry.addData("Turret Aim Position (ticks, zeroed)", getEncoderPosition());
        telemetry.addData("Turret Aim Zero (ticks)", encoderZero);
        telemetry.addData("Turret Aim Zero (rad)", ticksToRadians(encoderZero));
        telemetry.addData("Turret Aim Position (rad, zeroed)", getAimAngle());
    }
}
