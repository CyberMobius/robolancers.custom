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
public class KinectTankDrive extends CommandBase {

    public KinectTankDrive() {
        requires(drivetrain);
    }

    protected void initialize() {
        Drivetrain.driveMode = "Kinect - 2 Arm Tank Drive";
        drivetrain.stopAllMotors();
        System.out.println ("Kinect Tank Drive");
    }

    protected void execute() {
        drivetrain.kinectTankDrive();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
