package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * THESE ARE NOT PROPER VALUES, THEY ARE PLACEHOLDERS.
 * before tuning please make sure to replace with our actual values on the new FTC bot (26)
 */

// Import SwerveConfig, CoaxialPodConfig, and Controller from our swerve library
// (highkey don't know what the package is since we don't import that in our drive subsystem - ask neel)

public class Constants {
    public static SwerveConfig driveConfig = new SwerveConfig(
            c -> {
                c.zeroPowerBehavior.set(
                        SwerveConfig.ZeroPowerBehavior.IGNORE_ANGLE_CHANGES);
                c.manualBrakeMode.set(true);
                c.voltageCompensation.set(false);
            }
    );

    public static CoaxialPodConfig rightBack = new CoaxialPodConfig(c -> {
        c.name.set("rightBack");
        c.motorName.set("rb");
        c.servoName.set("rbTurn");
        c.servoEncoderName.set("rbTurnEncoder");
        c.turnController.set(Controller.pid(0.3, 0, 0.005)
                .plus(Controller.proportionalFeedforward(0)));
        c.driveDirection.set(DcMotorSimple.Direction.FORWARD);
        c.servoDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static CoaxialPodConfig leftFront = new CoaxialPodConfig(c -> {
        c.name.set("leftFront");
        c.motorName.set("lf");
        c.servoName.set("lfTurn");
        c.servoEncoderName.set("lfTurnEncoder");
        c.turnController.set(Controller.pid(0.3, 0, 0.005)
                .plus(Controller.proportionalFeedforward(0)));
        c.driveDirection.set(DcMotorSimple.Direction.FORWARD);
        c.servoDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static CoaxialPodConfig rightFront = new CoaxialPodConfig(c -> {
        c.name.set("rightFront");
        c.motorName.set("rf");
        c.servoName.set("rfTurn");
        c.servoEncoderName.set("rfTurnEncoder");
        c.turnController.set(Controller.pid(0.3, 0, 0.005)
                .plus(Controller.proportionalFeedforward(0)));
        c.driveDirection.set(DcMotorSimple.Direction.FORWARD);
        c.servoDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static CoaxialPodConfig leftBack = new CoaxialPodConfig(c -> {
        c.name.set("leftBack");
        c.motorName.set("lb");
        c.servoName.set("lbTurn");
        c.servoEncoderName.set("lbTurnEncoder");
        c.turnController.set(Controller.pid(0.3, 0, 0.0086)
                .plus(Controller.proportionalFeedforward(0)));
        c.driveDirection.set(DcMotorSimple.Direction.FORWARD);
        c.servoDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static Follower create(HardwareMap h) {
        // return Follower here once constructor is ready and the required drivetrain values known
        return null;
    }
}