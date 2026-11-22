package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.lib.pedro.Constants;
import org.firstinspires.ftc.teamcode.lib.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.TurretSubsystem;

public abstract class ShootParkAuto extends OpMode {
    private Follower drive;
    private TurretSubsystem turret;
    private IntakeSubsystem intake;

    private final Pose start;
    private final Pose path1;
    private final Pose path1Control1;

    abstract boolean isRedAlliance();

    public ShootParkAuto() {
        PoseFactory poseFactory = PoseFactory.degrees();

        if (!isRedAlliance()) {
            poseFactory = poseFactory.mirrorAroundPoint(new Pose(70.75, 70.75));
        }

        start = poseFactory.of(54.8415, 7.8345, 270);
        path1 = poseFactory.of(11.1503, 97.8959, 270);
        path1Control1 = poseFactory.of(11.0996, 13.8264, 0);

    }

    private Path shootToPark() {
        return Paths.curve(start, path1Control1, path1).linear(start, path1);
    }

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
        turret = new TurretSubsystem(hardwareMap, telemetry, isRedAlliance());
        intake = new IntakeSubsystem(hardwareMap, telemetry);
        drive.setPose(start);
        drive.update();
        Scheduler.reset();
    }

    @Override
    public void init_loop() {
        turret.calibrate();
        turret.periodic();
        telemetry.update();
    }

    @Override
    public void start() {
        schedule(
                sequential(
                        instant(() -> turret.shoot(5)),
                        waitMs(4000),
                        instant(() -> intake.setFlower()),
                        waitMs(4000),
                        instant(() -> {
                            turret.off();
                            intake.off();
                        }),
                        follow(drive, shootToPark())
                )
        );
    }

    @Override
    public void loop() {
        drive.update();
        Scheduler.execute();

        telemetry.addData("x", drive.pose().x());
        telemetry.addData("y", drive.pose().y());
        telemetry.addData("heading", drive.pose().heading());

        if (drive.currentPath() != null) {
            telemetry.addData("Current path distance remaining", drive.distanceToEndpoint());
            telemetry.addData("Path number", drive.pathIndex());
        }

        turret.periodic();
        telemetry.update();
    }
}
