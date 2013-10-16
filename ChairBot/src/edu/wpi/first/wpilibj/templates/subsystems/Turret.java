/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.turret.BothAirOff;

/**
 *
 * @author Eric
 */
public class Turret extends Subsystem {

    private final Relay leftAir, rightAir;

    public Turret() {
        leftAir = new Relay(RobotMap.RELAY_AIR_LEFT);
        rightAir = new Relay(RobotMap.RELAY_AIR_RIGHT);
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new BothAirOff());
    }

    public void leftAirOn() {
        leftAir.set(Relay.Value.kForward);
    }

    public void rightAirOn() {
        rightAir.set(Relay.Value.kForward);
    }

    public void rightAirOff() {
        rightAir.set(Relay.Value.kOff);
    }

    public void leftAirOff() {
        leftAir.set(Relay.Value.kOff);
    }

    public void bothAirOn() {
        rightAir.set(Relay.Value.kForward);
        leftAir.set(Relay.Value.kForward);
    }

    public void bothAirOff() {
        rightAir.set(Relay.Value.kOff);
        leftAir.set(Relay.Value.kOff);
    }
}
