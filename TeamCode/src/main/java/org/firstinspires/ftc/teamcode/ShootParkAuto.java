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
import org.firstinspires.ftc.teamcode.lib.subsystems.TurretSubsystem;

public abstract class ShootParkAuto extends OpMode {
    private Follower drive;
    private TurretSubsystem turret;

    private final Pose start;
    private final Pose path1;
    private final Pose point2;
    private final Pose point2Control1;

    abstract boolean isRedAlliance();

    public ShootParkAuto() {
        PoseFactory poseFactory = PoseFactory.degrees();

        if (!isRedAlliance()) {
            poseFactory = poseFactory.mirrorAroundPoint(new Pose(70.75, 70.75));
        }

        start = poseFactory.of(56, 8, 90);
        path1 = poseFactory.of(55.8345, 31.8626, 90);
        point2 = poseFactory.of(9.0758, 99.0169, 89.9041);
        point2Control1 = poseFactory.of(8.9837, 44.0143, 0);
    }


    private Path startToShoot() {
        return Paths.line(start, path1).linear(start, path1);

    }

    private Path shootToPark() {
        return Paths.curve(path1, point2Control1, point2).tangent();
    }

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
        turret = new TurretSubsystem(hardwareMap, telemetry, isRedAlliance());
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
                        follow(drive, startToShoot()),
                        instant(() -> turret.shoot(5)),
                        waitMs(5000),
                        instant(() -> turret.off()),
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
