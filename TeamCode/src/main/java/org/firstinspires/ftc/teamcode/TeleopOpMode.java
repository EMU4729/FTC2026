package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.lib.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.lib.subsystems.LEDSubsystem;

@TeleOp(name = "Teleop")
public class TeleopOpMode extends OpMode {
    private DriveSubsystem drive;
    private LEDSubsystem led;
    private final ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        drive = new DriveSubsystem(hardwareMap, telemetry);
        led = new LEDSubsystem(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void loop() {
        drive.driveRobotRelative(-gamepad2.left_stick_y, -gamepad2.left_stick_x, -gamepad2.right_stick_x);

        // TODO

        drive.periodic();
        led.periodic();
        telemetry.update();
    }
}
