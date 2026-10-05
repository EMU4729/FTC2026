package org.firstinspires.ftc.teamcode.lib.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    // TODO: config these and mark as `final`
    public static MecanumConfig mecanumConfig;
    public static PinpointConfig pinpointConfig;
    public static ForesightConfig foresightConfig;

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, pinpointConfig),
                new Mecanum(h, mecanumConfig),
                new Foresight(foresightConfig)
        );
    }
}
