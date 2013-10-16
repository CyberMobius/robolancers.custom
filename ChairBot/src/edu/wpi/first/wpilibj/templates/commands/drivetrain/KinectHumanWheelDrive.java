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
public class KinectHumanWheelDrive extends CommandBase {

    public KinectHumanWheelDrive() {
        requires(drivetrain);
    }

    protected void initialize() {
        Drivetrain.driveMode = "Kinect - Human Wheel Drive";
        drivetrain.stopAllMotors();
        System.out.println ("Kinect Human Wheel Drive");
    }

    protected void execute() {
        drivetrain.kinectHumanWheel();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
