package org.firstinspires.ftc.teamcode.lib.pedro;

import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

public class Tuning {
    @Tuner
    public static Procedure mecanumTuner() {
        return new PinpointTuner();
    }

    @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }

    @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner((hardwareMap) -> new PinpointLocalizer(hardwareMap, Constants.pinpointConfig), (hardwareMap) -> new Mecanum(hardwareMap, Constants.mecanumConfig));
    }

    @Tuner
    public static Procedure tests() {
        // First, start with this (after running the mecanumTuner standalone and setting Constants.mecanumConfig):
        return new Tests(hardwareMap -> new Mecanum(hardwareMap, Constants.mecanumConfig), null, null);
        // And then once Constants.pinpointConfig is set, run this:
        //return new Tests(hardwareMap -> new Mecanum(hardwareMap, Constants.mecanumConfig), (hardwareMap -> new PinpointLocalizer(hardwareMap, Constants.pinpointConfig)), null);
        // And then once Constants. foresightConfig is set, run this:
        //return new Tests(hardwareMap -> new Mecanum(hardwareMap, Constants.mecanumConfig), (hardwareMap -> new PinpointLocalizer(hardwareMap, Constants.pinpointConfig)), () -> new Foresight(Constants.foresightConfig));
    }
}
