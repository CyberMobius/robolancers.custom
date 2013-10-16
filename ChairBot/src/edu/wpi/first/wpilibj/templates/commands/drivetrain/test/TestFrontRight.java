/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain.test;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 * @author Eric
 */
public class TestFrontRight extends CommandBase {
    
    public TestFrontRight() {
        requires(drivetrain);
    }

    protected void initialize() {
    }

    protected void execute() {
        drivetrain.testFrontRight();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
    
    
}
