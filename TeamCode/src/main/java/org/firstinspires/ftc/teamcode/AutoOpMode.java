package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.pedro.Constants;
import org.firstinspires.ftc.teamcode.lib.subsystems.LEDSubsystem;

@Autonomous
public class AutoOpMode extends OpMode {
    private Follower drive;
    private LEDSubsystem led;
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
        led = new LEDSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        // TODO: Follow https://pedropathing.com/docs/pathing/guide/setting-up-auto after performing tuning

        led.periodic();
        telemetry.update();
    }
}
