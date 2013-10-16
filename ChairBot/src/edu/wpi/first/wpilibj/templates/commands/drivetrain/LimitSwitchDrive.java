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
public class LimitSwitchDrive extends CommandBase {

    private static boolean limitDriveMode = false;

    public LimitSwitchDrive() {
        requires(drivetrain);
    }

    protected void initialize() {
        limitDriveMode = true;
        drivetrain.stopAllMotors();
        System.out.println("Limit Switch Drive");
    }

    protected void execute() {
        drivetrain.limitSwitchDrive();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        limitDriveMode = false;
        System.out.println("Limit Switch Drive Off (End)");
    }

    protected void interrupted() {
        RecordStabilizeAngle.setStabilizeAngle(Drivetrain.getGyroAngle());
        limitDriveMode = false;
        System.out.println("Limit Switch Drive Off (Interrupted)");
    }

    public static boolean getLimitDriveMode() {
        return limitDriveMode;
    }
}
