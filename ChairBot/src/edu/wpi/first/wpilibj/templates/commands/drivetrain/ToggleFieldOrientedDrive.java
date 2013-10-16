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
public class ToggleFieldOrientedDrive extends CommandBase {

    private boolean isDone;
    private static int fieldDriveMode = Drivetrain.FIELD_DRIVE_OFF;

    public ToggleFieldOrientedDrive() {
        requires(drivetrain);

        isDone = false;
    }

    protected void initialize() {
        isDone = false;

        fieldDriveMode++;

        if (fieldDriveMode > 1) {
            fieldDriveMode = 0;
        }

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

    public static int getFieldDriveMode() {
        return fieldDriveMode;
    }

    public static void setFieldDriveMode(int mode) {
        fieldDriveMode = mode;
    }
}
