/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.leds;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 * @author Eric
 */
public class LEDsMode extends CommandBase {

    public static final int RED_ON = 0,
            BLUE_ON = 1,
            ALL_ON = 2,
            ALL_OFF = 3;

    public LEDsMode() {
        requires(LEDs);
    }

    protected void initialize() {
    }

    protected void execute() {
        switch (ToggleLEDs.getCounterLED()) {
            case (RED_ON):
                LEDs.redLEDsOn();
                break;
            case (BLUE_ON):
                LEDs.blueLEDsOn();
                break;
            case (ALL_ON):
                LEDs.allLEDsOn();
                break;
            case (ALL_OFF):
                LEDs.allLEDsOff();
                break;
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
