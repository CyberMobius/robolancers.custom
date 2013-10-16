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
public class KinectMindControl extends CommandBase {

    public KinectMindControl() {
        requires(drivetrain);
    }

    protected void initialize() {
        Drivetrain.driveMode = "MIND CONTROL";
        drivetrain.stopAllMotors();
        Drivetrain.mindPIDX.enable();
        Drivetrain.mindPIDZ.enable();
        System.out.println ("Kinect Mind Control");
    }

    protected void execute() {
        drivetrain.kinectMindControl();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
