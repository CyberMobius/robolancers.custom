/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;

/**
 *
 * @author Eric
 */
public class FieldDriveOff extends CommandBase {

    private boolean isDone;

    public FieldDriveOff() {
        isDone = false;
    }

    protected void initialize() {
        isDone = false;
        ToggleFieldOrientedDrive.setFieldDriveMode(Drivetrain.FIELD_DRIVE_OFF);
        System.out.println ("Field Drive Off");
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
}
