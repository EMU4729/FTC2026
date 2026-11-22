package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.pedro.Constants;
import org.firstinspires.ftc.teamcode.lib.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.TurretSubsystem;

public abstract class TeleopOpMode extends OpMode {
    private Follower drive;
    //    private LEDSubsystem led;
    private IntakeSubsystem intake;
    private TurretSubsystem turret;
    //    private boolean turretIsAuto = false;
    private boolean isFieldCentric = true;
    private boolean isShooting = false;

    private final ElapsedTime timer = new ElapsedTime();

    public abstract boolean isRedAlliance();

    @Override
    public void init() {
        drive = Constants.create(hardwareMap);
//        led = new LEDSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap, telemetry);
        turret = new TurretSubsystem(hardwareMap, telemetry, isRedAlliance());
    }

    @Override
    public void start() {
        timer.reset();
        turret.shoot(5);
    }

    @Override
    public void loop() {
        DrivePowers powers = isFieldCentric
                ? ManualDrive.fieldCentric(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, drive.pose().heading())
                : new DrivePowers(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        drive.manual(powers);
        drive.update();

        if (gamepad1.startWasPressed()) {
            isFieldCentric = !isFieldCentric;
        }

        if (gamepad1.backWasPressed()) {
            drive.setPose(drive.pose().withHeading(0));
        }

        if (gamepad1.left_trigger_pressed) {
            // this is actually flower for some reason
            intake.setGround();
        } else if (gamepad1.left_bumper) {
            // this is actually ground for some reason
            intake.setFlower();
        } else if (gamepad1.x) {
            intake.reverse();
        } else {
            intake.off();
        }

        if (isShooting) {
            turret.shoot(5);
        } else {
            turret.off();
        }

        if (gamepad1.rightTriggerWasPressed()) {
            isShooting = !isShooting;
        }

//        if (gamepad1.right_trigger_pressed) {
//            turret.shoot(5);
//        } else if (gamepad1.right_bumper) {
//            turret.dump();
//        } else {
//            turret.off();
//        }


//        if (gamepad1.xWasPressed() && Globals.turretIsCalibrated) {
//            turretIsAuto = !turretIsAuto;
//            turret.setAuto(turretIsAuto);
//        }

//        if (gamepad1.dpad_left) {
//            turret.setManualPower(-0.5);
//        } else if (gamepad1.dpad_right) {
//            turret.setManualPower(0.5);
//        } else {
//            turret.setManualPower(0);
//        }
//
//        if (!Globals.turretIsCalibrated) {
//            turret.calibrate();
//        }

//        led.periodic();
        intake.periodic();
        turret.periodic();
        telemetry.addData("Shooting?", isShooting);
        telemetry.addData("Field Relative Drive?", isFieldCentric);
        telemetry.update();
    }
}
