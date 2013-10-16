/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;

/**
 * Takes a snapshot of the angle whenever this method is called.
 * 
 * @author Eric
 */
public class RecordAngle extends CommandBase {

    private static double angle;
    
    public RecordAngle() {
        angle = 0;
    }
    
    protected void initialize() {
        angle = Drivetrain.getGyroAngle();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
    public static double getAngle() {
        return angle;
    }
}
