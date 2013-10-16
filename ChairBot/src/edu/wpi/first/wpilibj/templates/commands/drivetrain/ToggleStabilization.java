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
public class ToggleStabilization extends CommandBase {

    private static boolean stabilizeMode;
    private boolean isDone;
    
    public ToggleStabilization() {
        isDone = false;
        stabilizeMode = false;
    }
    
    protected void initialize() {
        isDone = false;
        if (stabilizeMode) { //If its in stabilizing mode and you press the button to toggle it, 
            System.out.println("Stabilization Off");
            stabilizeMode = false; //Stabilize mode will then turn off
        } else {
            System.out.println("Stabilization On");
            stabilizeMode = true;
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
    
    public static boolean isStabilizeMode() {
        return stabilizeMode;
    }
}
