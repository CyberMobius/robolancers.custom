/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.valves;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 * @author Eric
 */
public class BothValvesOn extends CommandBase {

    public BothValvesOn() {
        requires(valves);
    }

    protected void initialize() {
    }

    protected void execute() {
        valves.bothValvesOn();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
