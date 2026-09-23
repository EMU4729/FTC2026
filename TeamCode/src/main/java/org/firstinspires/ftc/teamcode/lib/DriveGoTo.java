package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.lib.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.PinpointLocalisationSubsystem;

public class DriveGoTo {
    private static final double TRANSLATION_P = 0.3; // TODO: tune
    private static final double TRANSLATION_FF = 0.2; // TODO: tune
    private static final double ROTATION_P = 0.2; // TODO: tune
    private static final double ROTATION_FF = 0.1; // TODO: tune
    private static final double TRANSLATION_THRESH = 0.1; // TODO: tune
    private static final double ROTATION_THRESH = 0.1; // TODO: tune

    private final DriveSubsystem drive;
    private final PinpointLocalisationSubsystem localisation;
    private final Pose2D target;

    public DriveGoTo(DriveSubsystem drive, PinpointLocalisationSubsystem localisation, Pose2D target) {
        this.drive = drive;
        this.localisation = localisation;
        this.target = target;
    }

    /**
     * Calculates the error of the current robot pose from the target
     *
     * @param robotPose The current robot pose
     * @return The error between the current robot pose and the target robot pose
     */
    private Pose2D getError(Pose2D robotPose) {
        return new Pose2D(
                DistanceUnit.METER,
                target.getX(DistanceUnit.METER) - robotPose.getX(DistanceUnit.METER),
                target.getY(DistanceUnit.METER) - robotPose.getY(DistanceUnit.METER),
                AngleUnit.RADIANS,
                target.getHeading(AngleUnit.RADIANS) - robotPose.getHeading(AngleUnit.RADIANS));
    }

    /**
     * Main looping logic for DriveGoTo. Should be called repeatedly until {@link DriveGoTo#atTarget()} returns true.
     */
    public void execute() {
        Pose2D robotPose = localisation.getPose();
        Pose2D error = getError(robotPose);

        double outputX = TRANSLATION_P * error.getX(DistanceUnit.METER);
        outputX += Math.copySign(outputX, TRANSLATION_FF);
        double outputY = TRANSLATION_P * error.getY(DistanceUnit.METER);
        outputY += Math.copySign(outputY, TRANSLATION_FF);
        double outputR = ROTATION_P * error.getHeading(AngleUnit.RADIANS);
        outputR += Math.copySign(outputR, ROTATION_FF);

        drive.driveFieldRelative(outputX, outputY, outputR, robotPose.getHeading(AngleUnit.RADIANS));
    }

    /**
     * @return true if the robot is within an acceptable error of the target position.
     */
    public boolean atTarget() {
        Pose2D robotPose = localisation.getPose();
        Pose2D error = getError(robotPose);
        return (Math.abs(error.getX(DistanceUnit.METER)) < TRANSLATION_THRESH &&
                Math.abs(error.getY(DistanceUnit.METER)) < TRANSLATION_THRESH &&
                Math.abs(error.getHeading(AngleUnit.RADIANS)) < ROTATION_THRESH);
    }
}
