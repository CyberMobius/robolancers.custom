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
public class ToggleLEDs extends CommandBase {

    private boolean isDone;
    private static int counterLED;

    public ToggleLEDs() {
        requires(LEDs);
        isDone = false;
        counterLED = LEDsMode.ALL_OFF;
    }

    protected void initialize() {
        isDone = false;
        counterLED++;
        if (counterLED > 3) {
            counterLED = 0;
        }
        isDone = true;
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isDone;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public static int getCounterLED() {
        return counterLED;
    }
}
