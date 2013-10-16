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
public class FieldDriveOn extends CommandBase {

    private boolean isDone;

    public FieldDriveOn() {
        isDone = false;
    }

    protected void initialize() {
        isDone = false;
        ToggleFieldOrientedDrive.setFieldDriveMode(Drivetrain.FIELD_DRIVE_ON);
        System.out.println ("Field Drive On");
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
