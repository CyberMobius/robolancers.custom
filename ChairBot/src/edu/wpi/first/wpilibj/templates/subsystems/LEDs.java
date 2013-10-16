/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.leds.LEDsMode;

/**
 * Probably not going to be used now, no LEDs are on the ChairBot.
 * 
 * @author Eric
 */
public class LEDs extends Subsystem {

    private final Relay red;
    private final Relay blue;

    public LEDs() {
        red = new Relay(RobotMap.RELAY_LED_RED);
        red.setDirection(Relay.Direction.kForward);
        blue = new Relay(RobotMap.RELAY_LED_BLUE);
        blue.setDirection(Relay.Direction.kForward);
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new LEDsMode());
    }

    public void redLEDsOn() {
        red.set(Relay.Value.kOn);
        blue.set(Relay.Value.kOff);
    }

    public void blueLEDsOn() {
        blue.set(Relay.Value.kOn);
        red.set(Relay.Value.kOff);
    }

    public void allLEDsOn() {
        red.set(Relay.Value.kOn);
        blue.set(Relay.Value.kOn);
    }

    public void allLEDsOff() {
        red.set(Relay.Value.kOff);
        blue.set(Relay.Value.kOff);
    }
}
