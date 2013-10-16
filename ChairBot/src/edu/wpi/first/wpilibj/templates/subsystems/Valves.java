/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.commands.valves.BothValvesOff;

/**
 *
 * Probably not going to be used now, no solenoids on the ChairBot.
 * 
 * @author Eric
 */
public class Valves extends Subsystem {

//    private final Solenoid leftValve, rightValve;
    private final DoubleSolenoid leftValve, rightValve;

    public Valves() {
//        leftValve = new Solenoid(RobotMap.SOLENOID_LEFT_VALVE);
//        rightValve = new Solenoid(RobotMap.SOLENOID_RIGHT_VALVE);
        leftValve = new DoubleSolenoid(1, 2);
        rightValve = new DoubleSolenoid(3, 4);
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new BothValvesOff());
    }

    public void leftValveOn() {
//        leftValve.set(true);
//        rightValve.set(false);
        leftValve.set(DoubleSolenoid.Value.kForward);
        rightValve.set(DoubleSolenoid.Value.kOff);
    }

    public void rightValveOn() {
//        rightValve.set(true);
//        leftValve.set(false);
        rightValve.set(DoubleSolenoid.Value.kForward);
        leftValve.set(DoubleSolenoid.Value.kOff);
    }

    public void bothValvesOn() {
//        leftValve.set(true);
//        rightValve.set(true);
        rightValve.set(DoubleSolenoid.Value.kForward);
        leftValve.set(DoubleSolenoid.Value.kForward);
    }

    public void bothValvesOff() {
//        leftValve.set(false);
//        rightValve.set(false);
        rightValve.set(DoubleSolenoid.Value.kOff);
        leftValve.set(DoubleSolenoid.Value.kOff);
    }
}
