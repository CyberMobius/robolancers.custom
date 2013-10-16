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
public class BothAirOff extends CommandBase {

    public BothAirOff() {
        requires(turret);
    }

    protected void initialize() {
    }

    protected void execute() {
        turret.bothAirOff();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
