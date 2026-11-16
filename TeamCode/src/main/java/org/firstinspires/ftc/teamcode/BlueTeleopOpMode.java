package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Blue Teleop")
public class BlueTeleopOpMode extends TeleopOpMode {
    public boolean isRedAlliance() {
        return false;
    }
}
