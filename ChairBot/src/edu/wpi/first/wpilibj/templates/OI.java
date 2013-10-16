package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.drivetrain.*;
import edu.wpi.first.wpilibj.templates.commands.turret.LeftAirOff;
import edu.wpi.first.wpilibj.templates.commands.turret.LeftAirOn;
import edu.wpi.first.wpilibj.templates.commands.turret.RightAirOff;
import edu.wpi.first.wpilibj.templates.commands.turret.RightAirOn;

public class OI {

    //Logitech Dual Action Controller
    public static Joystick driveStick = new Joystick(3);
    Button driveBtn1 = new JoystickButton(driveStick, 1),
            driveBtn2 = new JoystickButton(driveStick, 2),
            driveBtn3 = new JoystickButton(driveStick, 3),
            driveBtn4 = new JoystickButton(driveStick, 4),
            driveBtn5 = new JoystickButton(driveStick, 5),
            driveBtn6 = new JoystickButton(driveStick, 6),
            driveBtn7 = new JoystickButton(driveStick, 7),
            driveBtn8 = new JoystickButton(driveStick, 8),
            driveBtn9 = new JoystickButton(driveStick, 9),
            driveBtn10 = new JoystickButton(driveStick, 10),
            driveBtn11 = new JoystickButton(driveStick, 11),
            driveBtn12 = new JoystickButton(driveStick, 12);
    Button dPadUp = new Button() {

        public boolean get() {
            return (driveStick.getRawAxis(6) <= -0.75);
        }
    };
    Button dPadDown = new Button() {

        public boolean get() {
            return (driveStick.getRawAxis(6) >= 0.75);
        }
    };
    Button dPadRight = new Button() {

        public boolean get() {
            return (driveStick.getRawAxis(5) >= 0.75);
        }
    };
    Button dPadLeft = new Button() {

        public boolean get() {
            return (driveStick.getRawAxis(5) <= -0.75);
        }
    };
    Button driveStickMoving = new Button() {

        public boolean get() {
            return Math.abs(driveStick.getRawAxis(1)) >= 0.10 || Math.abs(driveStick.getRawAxis(2)) >= 0.10 
                    || Math.abs(driveStick.getRawAxis(3)) >= 0.10;
        }
    };
    Button driveStickStill = new Button() {

        public boolean get() {
            return (Math.abs(driveStick.getRawAxis(1)) < 0.10 && Math.abs(driveStick.getRawAxis(2)) < 0.10 
                    && Math.abs(driveStick.getRawAxis(3)) < 0.10);
//                    || (LimitSwitchDrive.getLimitDriveMode() && !Drivetrain.allPressed);
        }
    };
    Button enabled = new Button() {
      
        public boolean get() {
            return DriverStation.getInstance().isEnabled();
        }
    };
    public static KinectStick leftArm = new KinectStick(1);
    public static KinectStick rightArm = new KinectStick(2);
    public static Kinect kinect = Kinect.getInstance();
    public static double shoulderWidth, torsoHeight, armLength;
    public static String kinectStatus = "Pending"; //General information on what the Kinect is doing.
    public static String kinectDriveStatus, kinectRotationStatus; //Modified depending on the drive system being used. To be displayed on the SmartDashboard.

    public OI() {
        /*
         * Controls Drive Modes.
         */
//        driveBtn1.whenPressed(new KinectHumanWheelDrive()); //Not stable enough to use yet.
        driveBtn2.whenPressed(new JoystickControl());
        driveBtn3.whenPressed(new KinectPlayerPositionDrive());
        driveBtn4.whenPressed(new KinectTankDrive());
        driveBtn10.whenPressed(new KinectMindControl());
        
        /**
         * Test Motors. All set values are positive on this.
         */
//        driveBtn1.whileHeld(new TestRearLeft());
//        driveBtn2.whileHeld(new TestRearRight());
//        driveBtn3.whileHeld(new TestFrontRight());
//        driveBtn4.whileHeld(new TestFrontLeft());
        
        /*
         * Controls Solenoid Valves.
         */
//        dPadUp.whenPressed(new BothValvesOn());
//        dPadDown.whenPressed(new BothValvesOff());
//        dPadRight.whenPressed(new RightValveOn());
//        dPadLeft.whenPressed(new LeftValveOn());

        dPadLeft.whenPressed(new JoystickControl());
        dPadRight.whenPressed(new LimitSwitchDrive());

        driveBtn7.whenPressed(new LeftAirOn());
        driveBtn8.whenPressed(new RightAirOn());
        driveBtn5.whenPressed(new LeftAirOff());
        driveBtn6.whenPressed(new RightAirOff());
//        driveBtn1.whenPressed(new BothAirOn());

        dPadUp.whenPressed(new FieldDriveOn());
        dPadDown.whenPressed(new FieldDriveOff());
        driveBtn9.whenPressed(new ResetGyro()); //Press select to reset current heading to 0.

        driveBtn1.whenPressed(new ToggleStabilization());
//        driveStickMoving.whenPressed(new RecordAngle());
        driveStickStill.whenPressed(new RecordStabilizeAngle());
        
        enabled.whenPressed(new ResetEverything());
    }

    public static double getDriveX() {
        return -driveStick.getRawAxis(1);
    }

    public static double getDriveY() {
        return -driveStick.getRawAxis(2);
    }
    
    public static double getDriveRotation() {
        return -driveStick.getRawAxis(3);
    }

    /**
     * Taken from the KinectGestures class:
     *
     * This method takes an input, an input range, and an output range, and uses
     * them to scale and constrain the input to the output range
     *
     * @param input The input value to be manipulated
     * @param inputMin The minimum value of the input range
     * @param inputMax The maximum value of the input range
     * @param outputMin The minimum value of the output range
     * @param outputMax The maximum value of the output range
     * @return The output value scaled and constrained to the output range
     */
    public static double CoerceToRange(double input, double inputMin, double inputMax, double outputMin, double outputMax) {
        /*
         * Determine the center of the input range and output range
         */
        double inputCenter = Math.abs(inputMax - inputMin) / 2 + inputMin;
        double outputCenter = Math.abs(outputMax - outputMin) / 2 + outputMin;

        /*
         * Scale the input range to the output range
         */
        double scale = (outputMax - outputMin) / (inputMax - inputMin);
        /*
         * Apply the transformation
         */
        double result = (input + -inputCenter) * scale + outputCenter;

        /*
         * Constrain to the output range
         */
        return Math.max(Math.min(result, outputMax), outputMin);
    }

    public static double getRightX() {
        double rightAngle = 0;

        double shoulderCenterX = kinect.getSkeleton().GetShoulderCenter().getX();
        double shoulderCenterZ = kinect.getSkeleton().GetShoulderCenter().getZ();
        double handRightX = kinect.getSkeleton().GetHandRight().getX();
        double handRightZ = kinect.getSkeleton().GetHandRight().getZ();

        /*
         * Right arm angle calculation.
         */
        double rightX = handRightX - shoulderCenterX; //X distance from shoulder center to right hand.
        double rightZ = shoulderCenterZ - handRightZ; //Z distance from shoulder center to right hand.
        if (rightZ != 0) { //Prevents dividing by zero.
            rightAngle = Math.toDegrees(MathUtils.atan(rightZ / rightX));
        }

        if (rightAngle > 100 || rightAngle < -100) {
            rightAngle = 0;
        }

        rightAngle = CoerceToRange(rightAngle, -70, 70, -1, 1);

        if (rightAngle > 1) {
            rightAngle = 1;
        } else if (rightAngle < -1) {
            rightAngle = -1;
        }

        return rightAngle;
    }

    public static double getLeftX() {
        double leftAngle = 0;

        double shoulderCenterX = kinect.getSkeleton().GetShoulderCenter().getX();
        double shoulderCenterZ = kinect.getSkeleton().GetShoulderCenter().getZ();
        double handLeftX = kinect.getSkeleton().GetHandLeft().getX();
        double handLeftZ = kinect.getSkeleton().GetHandLeft().getZ();

        /*
         * Left arm angle calculation.
         */
        double leftX = shoulderCenterX - handLeftX; //X distance from shoulder center to left hand.
        double leftZ = shoulderCenterZ - handLeftZ; //Z distance from shoulder center to right hand.
        if (leftZ != 0) { //Prevents dividing by zero.
            leftAngle = Math.toDegrees(MathUtils.atan(leftZ / leftX));
        }

        if (leftAngle > 100 || leftAngle < -100) {
            leftAngle = 0;
        }

        leftAngle = CoerceToRange(leftAngle, -70, 70, -1, 1);

        if (leftAngle > 1) {
            leftAngle = 1;
        } else if (leftAngle < -1) {
            leftAngle = -1;
        }

        return leftAngle;
    }
}
