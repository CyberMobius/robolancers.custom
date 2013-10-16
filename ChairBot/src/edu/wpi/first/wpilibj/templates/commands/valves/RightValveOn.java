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
public class RightValveOn extends CommandBase {

    public RightValveOn() {
        requires(valves);
    }

    protected void initialize() {
    }

    protected void execute() {
        valves.rightValveOn();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
