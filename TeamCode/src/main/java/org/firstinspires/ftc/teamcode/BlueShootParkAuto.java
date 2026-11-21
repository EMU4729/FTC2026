package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Blue Shoot Park Auto")
public class BlueShootParkAuto extends ShootParkAuto {
    @Override
    boolean isRedAlliance() {
        return false;
    }
}
