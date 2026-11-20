package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.lib.pedro.Constants;

@Autonomous(name = "Move Left Only")
public class EmergencyLeftAutoOpMode extends OpMode {
    private Follower drive;

    @Override
    public void init() {
        Scheduler.reset();
        drive = Constants.create(hardwareMap);
    }

    @Override
    public void start() {
        schedule(sequential(
                instant(() -> drive.manual(0, -1, 0)),
                waitMs(200),
                instant(() -> drive.manual(0, 0, 0))
        ));
    }

    @Override
    public void loop() {
        Scheduler.execute();
    }
}
