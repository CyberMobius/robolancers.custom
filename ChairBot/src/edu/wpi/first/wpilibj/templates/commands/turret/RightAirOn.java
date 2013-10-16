/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.turret;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 * @author Eric
 */
public class RightAirOn extends CommandBase {

    public RightAirOn() {
        requires(turret);
    }

    protected void initialize() {
    }

    protected void execute() {
        turret.rightAirOn();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
