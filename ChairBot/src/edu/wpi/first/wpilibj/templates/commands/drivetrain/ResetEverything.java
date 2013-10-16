/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;

/**
 * Executed every time the robot is enabled to prevent the robot from going out of 
 * control as soon as it is enabled.
 * 
 * @author Eric
 */
public class ResetEverything extends CommandBase {
    
    private boolean isDone;
    
    public ResetEverything() {
        isDone = false;
    }

    protected void initialize() {
        isDone = false;
        
        Drivetrain.frontLeft.set(0);
        Drivetrain.frontRight.set(0);
        Drivetrain.rearLeft.set(0);
        Drivetrain.rearRight.set(0);
        
        RecordStabilizeAngle.setStabilizeAngle(Drivetrain.getGyroAngle());
        
//        ToggleFieldOrientedDrive.setFieldDriveMode(Drivetrain.FIELD_DRIVE_OFF);
        
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
