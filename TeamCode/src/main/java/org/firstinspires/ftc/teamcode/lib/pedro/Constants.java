package org.firstinspires.ftc.teamcode.lib.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static final MecanumConfig mecanumConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("driveFL");
        c.frontRightName.set("driveFR");
        c.backLeftName.set("driveRL");
        c.backRightName.set("driveRR");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig pinpointConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(8.581853551188793);
        c.yPodOffset.set(1.3072628111351192);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.5);
                Controller secondaryTranslationalForward = Controller.proportional(0.1);
                Controller primaryTranslationalLateral = Controller.proportional(2);
                Controller secondaryTranslationalLateral = Controller.proportional(1);
                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(1.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(1.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.007543879676903282));
                c.brake.set(Controller.proportionalFeedforward(0.00491229772536779));

                c.headingFeedback.set(Controller.proportional(2));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06035252450120475, 0.006993094018640782));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10480276200415375, 0.049147331099501534));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.514911694973018E-4, 0.0017553361138826994));

                c.maxAchievableForwardVelocity.set(63.29808437037061);
                c.maxAchievableStrafeVelocity.set(46.428043961510994);
                c.naturalForwardDeceleration.set(50.915013032198516);
                c.naturalStrafeDeceleration.set(86.69293652327117);
            });


    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, pinpointConfig),
                new Mecanum(h, mecanumConfig),
                new Foresight(foresightConfig)
        );
    }
}
