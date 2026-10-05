package org.firstinspires.ftc.teamcode.lib.pedro;

import android.util.Size;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagPositioner {
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
     * The AprilTag processor instance used to detect and track AprilTags.
     */
    private final AprilTagProcessor aprilTag;

    /**
     * The VisionPortal managing the camera pipeline and stream settings.
     */
    private final VisionPortal visionPortal;

    /**
     * Whether the follower's pose has been initialized from an AprilTag yet or not.
     */
    private boolean isPositioned = false;

    public AprilTagPositioner(HardwareMap hardwareMap) {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(CAMERA_POSITION, CAMERA_ORIENTATION)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(1280, 800))
                .addProcessor(aprilTag)
                .build();
    }

    /**
     * Tries to set the position of the provided {@link Follower} using any detected AprilTag.
     *
     * @param drive The {@link Follower} used for driving.
     * @return Whether the positioning succeeded or not.
     */
    public boolean tryPositioning(Follower drive) {
        List<AprilTagDetection> freshDetections = aprilTag.getFreshDetections();
        if (freshDetections == null || freshDetections.isEmpty()) return false;
        for (AprilTagDetection detection : freshDetections) {
            if (detection.robotPose == null) continue;
            Pose detectedPose = new Pose(
                    detection.robotPose.getPosition().x,
                    detection.robotPose.getPosition().y,
                    detection.robotPose.getOrientation().getYaw(AngleUnit.RADIANS)
            );
            drive.setPose(detectedPose);
            isPositioned = true;
            return true;
        }
        return false;
    }

    /**
     * @return Whether positioning (via {@link tryPositioning}) has succeeded once before.
     */
    public boolean isPositioned() {
        return isPositioned;
    }
}
