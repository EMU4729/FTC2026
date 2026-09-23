package org.firstinspires.ftc.teamcode.lib.subsystems;

import android.util.Size;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class PinpointLocalisationSubsystem {
    private static final double X_POD_OFFSET_MM = -84.0; // honestly not sure how to do this lol. using "tuned for 3110-0002-0001 Product Insight #1"
    private static final double Y_POD_OFFSET_MM = -168.0; // edit these variables to pass through to pinpoint.setOffset
    private static final Position CAMERA_POSITION = new Position(
            DistanceUnit.METER, -0.13, -0.16, 0.045, 0);
    private static final YawPitchRollAngles CAMERA_ORIENTATION = new YawPitchRollAngles(AngleUnit.DEGREES,
            180, -45, 0, 0);

    private final Telemetry telemetry;
    private final GoBildaPinpointDriver pinpoint;
    private final AprilTagProcessor aprilTag;
    private final VisionPortal visionPortal;
    private final IMU imu;
    private boolean initialised = false;

    public PinpointLocalisationSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;

        // setup vision
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(CAMERA_POSITION, CAMERA_ORIENTATION)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(1280, 800))
                .addProcessor(aprilTag)
                .build();

        // setup pinpoint
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD); // 2000 CPR w/ 32mm dia wheel - 2000/100.53 (circum) = 19.894
        pinpoint.setOffsets(X_POD_OFFSET_MM, Y_POD_OFFSET_MM, DistanceUnit.MM);
        pinpoint.resetPosAndIMU();

        // starting position, will be updated by april tag positioning.
        pinpoint.setPosition();

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        )));
    }

    /**
     * @return The yaw, pitch and roll angles, as reported by the IMU.
     */
    public YawPitchRollAngles getIMUAngles() {
        return imu.getRobotYawPitchRollAngles();
    }

    /**
     * @return true if the robot's pose has been initialised from a detected AprilTag, false if not
     */
    public boolean isInitialised() {
        return initialised;
    }

    /**
     * @return The current pose of the robot.
     */
    public Pose2D getPose() {
        return pinpoint.getPosition();
    }

    private void updateTelemetry() {
        telemetry.addData("AprilTag Positioning Complete", initialised);
        telemetry.addData("Device Status", pinpoint.getDeviceStatus());
        telemetry.addData("Robot Pose X (m)", pinpoint.getPosition().getX(DistanceUnit.METER));
        telemetry.addData("Robot Pose Y (m)", pinpoint.getPosition().getY(DistanceUnit.METER));
        telemetry.addData("Robot Heading (rad)", pinpoint.getHeading(AngleUnit.RADIANS));
    }

    public void periodic() {
        pinpoint.update();
        updateTelemetry(); // updating telem AFTER updating pinpoint, old method called before updating

        // early return if we don't need to do apriltag stuff anymore
        if (initialised) return;

        List<AprilTagDetection> freshDetections = aprilTag.getFreshDetections();
        if (freshDetections == null || freshDetections.isEmpty()) return;
        AprilTagDetection detection = freshDetections.get(0);

        // handle localisation initialisation
        if (!initialised && detection.robotPose != null) {
            Pose2D tagPose = new Pose2D(
                    DistanceUnit.METER,
                    detection.robotPose.getPosition().x,
                    detection.robotPose.getPosition().y,
                    AngleUnit.RADIANS,
                    detection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)
            );
            // Pass newly calibrated pose to Pinpoint computer
            pinpoint.setPosition(tagPose);
            initialised = true;
        }
    }
}
