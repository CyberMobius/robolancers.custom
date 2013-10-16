/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.drivetrain.ToggleFieldOrientedDrive;
import edu.wpi.first.wpilibj.templates.commands.drivetrain.ToggleStabilization;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;

/**
 * Chairbot.
 * 
 * @author Eric
 */
public class RobotTemplate extends IterativeRobot {

    /*
     * For the Kinect Kiosk, first the Kinect is elevated to a height of 44
     * inches.
     *
     * Then a distance of 60 inches is measured where then, the top of a square
     * which will dictate the human player's space is made. This square is 72
     * inches squared.
     *
     * Another box is then made at the center of this square which measures 36
     * inches squared. This box dictates the neutral zone during the "Kinect -
     * Human Position Drive".
     *
     * Further borderlines should be made past all these borderlines to dictate
     * that people should not come within the boundaries that could cause the
     * Kinect to start tracking other people instead of a desired single player.
     */
    public void robotInit() {
        CommandBase.init();
        dashboardInit();
        updateDashboard();
    }

    public void autonomousInit() {
        updateDashboard();
    }

    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    public void autonomousContinuous() {
    }

    public void teleopInit() {
        updateDashboard();
    }

    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    public void teleopContinuous() {
    }

    public void disabledInit() {
        updateDashboard();
    }

    public void disabledPeriodic() {
        updateDashboard();
    }

    public void disabledContinuous() {
    }

    private void dashboardInit() {
        SmartDashboard.putData("mindPIDX", Drivetrain.mindPIDX);
        SmartDashboard.putData("mindPIDZ", Drivetrain.mindPIDZ);
    }
    
    public static double rotationCorrection = 0;

    private void updateDashboard() {
        SmartDashboard.putString("Drive Mode", Drivetrain.driveMode);

        SmartDashboard.putDouble("X Speed", Drivetrain.xSpeed);
        SmartDashboard.putDouble("Y Speed", Drivetrain.ySpeed);
        SmartDashboard.putDouble("Rotation Speed", Drivetrain.rotationSpeed);
        
        SmartDashboard.putDouble("Front Left Motor", Drivetrain.frontLeft.get());
        SmartDashboard.putDouble("Front Right Motor", Drivetrain.frontRight.get());
        SmartDashboard.putDouble("Rear Left Motor", Drivetrain.rearLeft.get());
        SmartDashboard.putDouble("Rear Right Motor", Drivetrain.rearRight.get());
        
        SmartDashboard.putDouble("X Axis Input", OI.getDriveX());
        SmartDashboard.putDouble("Y Axis Input", OI.getDriveY());
        SmartDashboard.putDouble("Rotation Axis Input", OI.getDriveRotation());
        
        SmartDashboard.putBoolean("Stabilization Mode", ToggleStabilization.isStabilizeMode());
        
        boolean fieldDriveMode = (ToggleFieldOrientedDrive.getFieldDriveMode() == Drivetrain.FIELD_DRIVE_ON) ? true : false; 
        
        SmartDashboard.putBoolean("Field Drive Mode", fieldDriveMode);
        
        SmartDashboard.putDouble("Rotation Correction", rotationCorrection);
        
        SmartDashboard.putDouble("Gyro Angle", Drivetrain.getGyroAngle());
        
        SmartDashboard.putDouble("Smooth Drive PID Setpoint", Drivetrain.smoothDrivePID.getSetpoint());
        SmartDashboard.putDouble("Smooth Drive PID Error", Drivetrain.smoothDrivePID.getError());
        SmartDashboard.putDouble("Smooth Drive PID Result", Drivetrain.smoothDrivePID.get());
                
//        updateDashboardTemp();
    }

    private void updateDashboardTemp() {
        SmartDashboard.putDouble("PID X result", Drivetrain.mindPIDX.m_result);
        SmartDashboard.putDouble("PID Z result", Drivetrain.mindPIDZ.m_result);

        SmartDashboard.putDouble("PID X error", Drivetrain.mindPIDX.getError());
        SmartDashboard.putDouble("PID Z error", Drivetrain.mindPIDZ.getError());

        SmartDashboard.putBoolean("PID X onTarget", Drivetrain.mindPIDX.onTarget());
        SmartDashboard.putBoolean("PID Z onTarget", Drivetrain.mindPIDZ.onTarget());

        SmartDashboard.putDouble("PIDX get", OI.kinect.getPosition().getX());
        SmartDashboard.putDouble("PIDZ get", (OI.kinect.getSkeleton().GetHandLeft().getZ() + OI.kinect.getSkeleton().GetHandRight().getZ()) / 2);

        SmartDashboard.putDouble("Left Motor get", Drivetrain.frontLeft.get());
        SmartDashboard.putDouble("Right Motor get", Drivetrain.rearLeft.get());

        SmartDashboard.putDouble("Gyro Angle", Drivetrain.getGyroAngle());
    }
}
