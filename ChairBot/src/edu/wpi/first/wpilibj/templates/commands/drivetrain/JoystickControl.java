/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands.drivetrain;

import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.subsystems.Drivetrain;

/**
 *
 * @author Eric
 */
public class JoystickControl extends CommandBase {

    public JoystickControl() {
        requires(drivetrain);
    }

    protected void initialize() {
        Drivetrain.driveMode = "Joystick Control";
        drivetrain.stopAllMotors();
        System.out.println ("Joystick Control");
    }

    protected void execute() {
        double angle = 0;

        switch (ToggleFieldOrientedDrive.getFieldDriveMode()) {
            case (Drivetrain.FIELD_DRIVE_ON):
                angle = Drivetrain.getGyroAngle();
                break;

            case (Drivetrain.FIELD_DRIVE_OFF):
                angle = 0;
                break;
        }

        if (Math.abs(OI.driveStick.getRawAxis(1)) >= 0.10 || Math.abs(OI.driveStick.getRawAxis(2)) >= 0.10
                || Math.abs(OI.driveStick.getRawAxis(3)) >= 0.10) {
            drivetrain.joystickControlWithCorrection(angle);
        } else if (ToggleStabilization.isStabilizeMode()) {
            drivetrain.stabilizeHeading(RecordStabilizeAngle.getStabilizeAngle());
        } else {
            drivetrain.stopAllMotors();
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
