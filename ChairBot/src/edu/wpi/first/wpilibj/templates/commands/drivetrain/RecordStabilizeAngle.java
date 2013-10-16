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
public class RecordStabilizeAngle extends CommandBase {
    
    private static double stabilizeAngle;
    private boolean isDone;
    
    public RecordStabilizeAngle() {
        stabilizeAngle = 0;
        
        isDone = false;
    }

    protected void initialize() {
        isDone = false;
        stabilizeAngle = Drivetrain.getGyroAngle();
        isDone = true;
        System.out.println ("Recorded Stabilizing Angle");
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
    
    public static double getStabilizeAngle() {
        return stabilizeAngle;
    }
    
    public static void setStabilizeAngle(double angle) {
        stabilizeAngle = angle;
    }
}
