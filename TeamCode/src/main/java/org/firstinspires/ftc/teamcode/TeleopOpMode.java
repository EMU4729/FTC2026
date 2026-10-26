package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.pedro.AprilTagPositioner;
import org.firstinspires.ftc.teamcode.lib.pedro.Constants;
import org.firstinspires.ftc.teamcode.lib.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.LEDSubsystem;

import kotlin.ArrayIntrinsicsKt;

@TeleOp(name = "Teleop")
public class TeleopOpMode extends OpMode {
    private Follower drive;
    private AprilTagPositioner aprilTagPositioner;
    private LEDSubsystem led;
    private IntakeSubsystem intake;
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
        aprilTagPositioner = new AprilTagPositioner(hardwareMap);
        led = new LEDSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        if (!aprilTagPositioner.isPositioned()) aprilTagPositioner.tryPositioning(drive);

        drive.manual(-gamepad2.left_stick_y, -gamepad2.left_stick_x, gamepad2.right_stick_x);
        drive.update();

        if (gamepad1.b) {
            intake.intakeOff();
        }

        led.periodic();
        intake.periodic();
        telemetry.update();
    }
}
