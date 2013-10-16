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
public class KinectPlayerPositionDrive extends CommandBase {

    public KinectPlayerPositionDrive() {
        requires(drivetrain);
    }

    protected void initialize() {
        Drivetrain.driveMode = "Kinect - Player Position Drive";
        drivetrain.stopAllMotors();
        System.out.println ("Kinect Player Position Drive");
    }

    protected void execute() {
        double angle = 0;

        switch (ToggleFieldOrientedDrive.getFieldDriveMode()) {
            case (Drivetrain.FIELD_DRIVE_ON):
                angle = drivetrain.getGyroAngle();
                break;

            case (Drivetrain.FIELD_DRIVE_OFF):
                angle = 0;
                break;
        }

        drivetrain.kinectPlayerPositionDrive(angle);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
