package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Red Shoot Park Auto")
public class RedShootParkAuto extends ShootParkAuto {
    @Override
    boolean isRedAlliance() {
        return true;
    }
}
