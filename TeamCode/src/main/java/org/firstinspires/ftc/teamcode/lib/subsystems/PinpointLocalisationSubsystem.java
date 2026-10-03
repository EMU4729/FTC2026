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
public class PinpointLocalisationSubsystem implements Subsystem {
    /**
     * The offset of the X pod from the center of the robot, in millimeters.
     */
    private static final double X_POD_OFFSET_MM = -84.0;
    /**
     * The offset of the Y pod from the center of the robot, in millimeters.
     */
    private static final double Y_POD_OFFSET_MM = -168.0;
    /**
     * The 3D position offset of the camera relative to the center of the robot.
     */
    private static final Position CAMERA_POSITION = new Position(
            DistanceUnit.METER, -0.13, -0.16, 0.045, 0);
    /**
     * The 3D orientation offset (yaw, pitch, roll) of the camera relative to the robot's coordinate system.
     */
    private static final YawPitchRollAngles CAMERA_ORIENTATION = new YawPitchRollAngles(AngleUnit.DEGREES,
            180, -45, 0, 0);

    /**
     * The FTC telemetry instance used to stream diagnostic data to the Driver Station.
     */
    private final Telemetry telemetry;
    /**
     * The Pinpoint instance used to track the robot's position.
     */
    private final GoBildaPinpointDriver pinpoint;
    /**
     * The AprilTag processor instance used to detect and track AprilTags.
     */
    private final AprilTagProcessor aprilTag;
    /**
     * The VisionPortal managing the camera pipeline and stream settings.
     */
    private final VisionPortal visionPortal;
    /**
     * The IMU instance used to measure the robot's orientation.
     */
    private final IMU imu;
    /**
     * Tracks whether the robot pose has been successfully initialized from an AprilTag detection.
     * Starts as false and is set to true once a valid AprilTag detection is processed.
     */
    private boolean initialised = false;

    /**
     * Creates a new instance of the {@code PinpointLocalisationSubsystem}.
     *
     * @param hardwareMap The hardware map of the robot.
     * @param telemetry The FTC telemetry instance used to stream diagnostic data to the Driver Station.
     */
    public PinpointLocalisationSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry; // Binds telemetry from constructor for further use.

        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(CAMERA_POSITION, CAMERA_ORIENTATION)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(1280, 800))
                .addProcessor(aprilTag)
                .build();

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD); // 2000 CPR w/ 32mm dia wheel - 2000/100.53 (circum) = 19.894
        pinpoint.setOffsets(X_POD_OFFSET_MM, Y_POD_OFFSET_MM, DistanceUnit.MM);
        pinpoint.resetPosAndIMU();
        /**
         * Retrieves REV Control/Expansion Hub IMU sensor instance from hardware map.
         *
         * @param IMU.class The generic hardware interface for built-in inertia measurement.
         * @param "imu"     The config name for the IMU.
         */
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

    //Updates telem info for Driver Station.
    private void updateTelemetry() {
        telemetry.addData("AprilTag Positioning Complete", initialised);
        telemetry.addData("Device Status", pinpoint.getDeviceStatus());
        telemetry.addData("Robot Pose X (m)", getPose().getX(DistanceUnit.METER));
        telemetry.addData("Robot Pose Y (m)", getPose().getY(DistanceUnit.METER));
        telemetry.addData("Robot Heading (rad)", getPose().getHeading(AngleUnit.RADIANS));
    }

    /**
     * Subsystem execution cycle called repeatedly inside OpMode loop
     * Handles updating hardware odometry buffers, pushing telem data, and evaling initial AprilTag(s).
     */
    @Override
    public void periodic() {
        pinpoint.update();
        updateTelemetry();

        if (initialised) return; // If robot pose already initalised, skip AprilTag detection and calibration

        List<AprilTagDetection> freshDetections = aprilTag.getFreshDetections();
        if (freshDetections == null || freshDetections.isEmpty()) return;
        for (AprilTagDetection detection : freshDetections) {
            if (detection.robotPose == null) continue;
            Pose2D tagPose = new Pose2D(
                    DistanceUnit.METER,
                    detection.robotPose.getPosition().x,
                    detection.robotPose.getPosition().y,
                    AngleUnit.RADIANS,
                    detection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)
            );
            pinpoint.setPosition(tagPose); // Updates the robot's pose based on the detected AprilTag's pose
            initialised = true;
            break;
        }
    }
}
