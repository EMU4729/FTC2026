package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.pedro.Constants;
import org.firstinspires.ftc.teamcode.lib.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.LEDSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.TurretSubsystem;

public abstract class TeleopOpMode extends OpMode {
    private Follower drive;
    private LEDSubsystem led;
    private IntakeSubsystem intake;
    private TurretSubsystem turret;
    private boolean turretIsAuto = false;

    private final ElapsedTime timer = new ElapsedTime();

    public abstract boolean isRedAlliance();

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
        led = new LEDSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry);
        turret = new TurretSubsystem(hardwareMap, telemetry, isRedAlliance());
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        drive.manual(-gamepad2.left_stick_y, -gamepad2.left_stick_x, gamepad2.right_stick_x);
        drive.update();

        if (gamepad1.left_trigger_pressed) {
            intake.setGround();
        } else if (gamepad1.left_bumper) {
            intake.setFlower();
        } else {
            intake.off();
        }

        if (gamepad1.right_trigger_pressed) {
            turret.shoot();
        } else if (gamepad1.right_bumper) {
            turret.dump();
        } else {
            turret.off();
        }

        if (gamepad1.xWasPressed() && turret.isCalibrated()) {
            turretIsAuto = !turretIsAuto;
            turret.setAuto(turretIsAuto);
        }

        if (gamepad1.dpad_left) {
            turret.setManualPower(-0.5);
        } else if (gamepad1.dpad_right) {
            turret.setManualPower(0.5);
        } else {
            turret.setManualPower(0);
        }

        if (!turret.isCalibrated()) {
            turret.calibrate();
        }

        led.periodic();
        intake.periodic();
        turret.periodic();
        telemetry.update();
    }
}
