/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 * @author Eric
 */
public class ResetGyro extends CommandBase {

    private boolean isDone;
    
    public ResetGyro() {
        isDone = false;
    }

    protected void initialize() {
        isDone = false;
        System.out.println("Gyro Reset");
        drivetrain.resetGyro();
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
