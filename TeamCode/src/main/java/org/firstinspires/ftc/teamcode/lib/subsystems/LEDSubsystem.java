package org.firstinspires.ftc.teamcode.lib.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.prism.Color;
import org.firstinspires.ftc.teamcode.lib.prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.lib.prism.PrismAnimations;

public class LEDSubsystem implements Subsystem {
    private static final boolean DISABLE = true;
    private final GoBildaPrismDriver leds;
    private final PrismAnimations.Solid animation = new PrismAnimations.Solid(Color.TRANSPARENT);

    public LEDSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        leds = hardwareMap.get(GoBildaPrismDriver.class, "leds");
        leds.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, animation);
    }

    /**
     * Sets the LEDs to a solid color - also modifies the subsystem's mode.
     *
     * @param color The desired color.
     */
    public void setColor(Color color) {
        animation.setPrimaryColor(color);
        leds.insertAndUpdateAnimation(GoBildaPrismDriver.LayerHeight.LAYER_0, animation);
    }
}
