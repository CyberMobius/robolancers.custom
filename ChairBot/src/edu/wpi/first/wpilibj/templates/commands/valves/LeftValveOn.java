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
public class LeftValveOn extends CommandBase {

    public LeftValveOn() {
        requires(valves);
    }

    protected void initialize() {
    }

    protected void execute() {
        valves.leftValveOn();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
