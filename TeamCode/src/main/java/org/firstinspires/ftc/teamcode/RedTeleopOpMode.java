package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Red Teleop")
public class RedTeleopOpMode extends TeleopOpMode {
    public boolean isRedAlliance() {
        return true;
    }
}
