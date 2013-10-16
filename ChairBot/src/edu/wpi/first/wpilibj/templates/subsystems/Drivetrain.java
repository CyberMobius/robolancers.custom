 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.RobotTemplate;
import edu.wpi.first.wpilibj.templates.commands.drivetrain.JoystickControl;
import edu.wpi.first.wpilibj.templates.commands.drivetrain.RecordStabilizeAngle;
import edu.wpi.first.wpilibj.templates.custom.PIDWithoutOutput;
import edu.wpi.first.wpilibj.templates.custom.SendablePIDController;
import edu.wpi.first.wpilibj.templates.kinect.KinectHumanPositionDrive;
import edu.wpi.first.wpilibj.templates.kinect.KinectSteeringWheelDrive;

/**
 *
 * @author Eric
 */
public class Drivetrain extends Subsystem {

    public static double mindPIDXGet, mindPIDZGet;

    private class MindPIDXSource implements PIDSource {

        public double pidGet() {
            mindPIDXGet = kinect.getPosition().getX();
            return mindPIDXGet;
        }
    }

    private class MindPIDZSource implements PIDSource {

        public double pidGet() {
            mindPIDZGet = (kinect.getSkeleton().GetHandLeft().getZ() + kinect.getSkeleton().GetHandRight().getZ()) / 2; //Average Hand Z Distance.
            return mindPIDZGet;
        }
    }
    public static final int FIELD_DRIVE_ON = 0,
            FIELD_DRIVE_OFF = 1;
    private final double P_HEADING = 0.05,
            I_HEADING = 0,
            D_HEADING = 0,
            P_SMOOTH = 0,
            I_SMOOTH = 0.1,
            D_SMOOTH = 0,
            P_MIND_X = 0.65,
            I_MIND_X = 0,
            D_MIND_X = 0,
            P_MIND_Z = 0.35,
            I_MIND_Z = 0,
            D_MIND_Z = 0,
            MAX_LIMIT_SWITCH_DRIVE_OUTPUT = 0.30,
            MAX_DRIVE_OUTPUT = 0.85; //Base max drive output when used in conjunction with drive correction method.
    private final boolean PRESSED = false,
            NOT_PRESSED = true;
    public static Jaguar frontLeft, rearLeft, frontRight, rearRight; //Momentarily public static, switch back to private final later
    private static Gyro gyro;
    private final DigitalInput forward, backward, left, right, rotationLeft, rotationRight;
    private final RobotDrive drive;
    private final MindPIDXSource mindPIDXSource;
    private final MindPIDZSource mindPIDZSource;
    public static SendablePIDController mindPIDZ, mindPIDX;
    public static PIDWithoutOutput headingPID, smoothDrivePID;
    private final Kinect kinect = OI.kinect; //Initialized here to prevent a null pointer exception.
    private final KinectSteeringWheelDrive wheelDrive;
    private final KinectHumanPositionDrive posDrive;
    public static boolean allPressed = false;
    public static String driveMode;
    public static String verticalDriveStatus = "Pending", horizontalDriveStatus = "Pending", rotationStatus = "Pending"; //From the perspective of an overhead view, shows which way robot is driving.
    public static double xSpeed = 0, ySpeed = 0, rotationSpeed = 0;

    public Drivetrain() {
        frontLeft = new Jaguar(RobotMap.MOTOR_FRONT_LEFT);
        rearLeft = new Jaguar(RobotMap.MOTOR_REAR_LEFT);
        frontRight = new Jaguar(RobotMap.MOTOR_FRONT_RIGHT);
        rearRight = new Jaguar(RobotMap.MOTOR_REAR_RIGHT);

        gyro = new Gyro(RobotMap.ANALOG_GYRO);

        left = new DigitalInput(RobotMap.LIMIT_LEFT);
        backward = new DigitalInput(RobotMap.LIMIT_BACKWARD);
        right = new DigitalInput(RobotMap.LIMIT_RIGHT);
        forward = new DigitalInput(RobotMap.LIMIT_FORWARD);
        rotationLeft = new DigitalInput(RobotMap.LIMIT_ROTATION_LEFT);
        rotationRight = new DigitalInput(RobotMap.LIMIT_ROTATION_RIGHT);

        drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight); //For mecanum wheeled robots
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setSafetyEnabled(false);

        mindPIDXSource = new MindPIDXSource();
        mindPIDX = new SendablePIDController(P_MIND_X, I_MIND_X, D_MIND_X, mindPIDXSource, frontLeft);
        mindPIDX.setInputRange(-2, 2); //4
        mindPIDX.setTolerance(9);
        mindPIDX.setSetpoint(0);
        mindPIDX.disable();

        mindPIDZSource = new MindPIDZSource();
        mindPIDZ = new SendablePIDController(P_MIND_Z, I_MIND_Z, D_MIND_Z, mindPIDZSource, frontLeft);
        mindPIDZ.setInputRange(1.5, 3.5); //2
        mindPIDZ.setTolerance(12.5); //Be within 0.5 m
        mindPIDZ.setSetpoint(2.5);
        mindPIDZ.disable();

        headingPID = new PIDWithoutOutput(P_HEADING, I_HEADING, D_HEADING, gyro);
        headingPID.enable();

        smoothDrivePID = new PIDWithoutOutput(P_SMOOTH, I_SMOOTH, D_SMOOTH, gyro);
        smoothDrivePID.enable();

        wheelDrive = new KinectSteeringWheelDrive();
        posDrive = new KinectHumanPositionDrive();

        driveMode = "Pending";
    }

    protected void initDefaultCommand() {
        setDefaultCommand(new JoystickControl());
    }

    /**
     * My logic is very hard to follow with all the inputs being reversed
     * through several methods... Sorry...
     *
     * @param angle Angle to be used in the mecanum drive method.
     */
    public void joystickControl(double angle) {
        double xInput = 0, yInput = 0, rotationInput = 0;

        if (OI.getDriveX() >= 0.10) {
            xInput = OI.CoerceToRange(OI.getDriveX(), 0.10, 1, 0, 1);
        } else if (OI.getDriveX() <= -0.10) {
            xInput = OI.CoerceToRange(OI.getDriveX(), -1, -0.10, -1, 0);
        }

        if (OI.getDriveY() >= 0.10) {
            yInput = OI.CoerceToRange(OI.getDriveY(), 0.10, 1, 0, 1);
        } else if (OI.getDriveY() <= -0.10) {
            yInput = OI.CoerceToRange(OI.getDriveY(), -1, -0.10, -1, 0);
        }

        if (OI.getDriveRotation() >= 0.10) {
            rotationInput = OI.CoerceToRange(OI.getDriveRotation(), 0.10, 1, 0, 1);
        } else if (OI.getDriveRotation() <= -0.10) {
            rotationInput = OI.CoerceToRange(OI.getDriveRotation(), -1, -0.10, -1, 0);
        }

        drive.mecanumDrive_Cartesian(xInput, yInput, rotationInput, angle);
//        drive.arcadeDrive(yInput, rotationInput);
    }

    public static double getGyroAngle() {
        return gyro.getAngle();
    }

    public void resetGyro() {
        gyro.reset();
    }

    public void kinectTankDrive() {
        if (kinect.getSkeleton().GetTrackState() != Skeleton.tTrackState.kNotTracked && isMostlyTracked()) {
            double leftArmInput = OI.leftArm.getY(); //Inverted so that when arm is all the way up, returns 1
            double rightArmInput = -OI.rightArm.getY();

            setDriveStatus(3, 2, 1); //Just to symbolize its in Tank Drive.
            drive.tankDrive(leftArmInput, rightArmInput);
        } else {
            setDriveStatus(0, 0, 0);
        }
    }

    public void kinectPlayerPositionDrive(double angle) {
        if (kinect.getSkeleton().GetTrackState() != Skeleton.tTrackState.kNotTracked && isMostlyTracked()) {
            double xInput = -posDrive.getMecanumX();
            double yInput = posDrive.getMecanumY();
            double rotationInput = -posDrive.getArmRotation();

            setDriveStatus(xInput, yInput, rotationInput);
            drive.mecanumDrive_Cartesian(xInput, yInput, rotationInput, angle); //For mecanum wheeled robot.
//            drive.arcadeDrive(yInput, rotationInput); //For non-mecanum wheeled robot
        } else {
            setDriveStatus(0, 0, 0);
        }
    }

    /**
     * Control rotation of the robot via "steering wheel" and move forwards and
     * backwards by walking forwards or backwards. If we're using mecanum
     * wheels, walk side to side to move side to side.
     */
    public void kinectHumanWheel() { //TODO Calibration rotation.
        if (kinect.getSkeleton().GetTrackState() != Skeleton.tTrackState.kNotTracked && isMostlyTracked()) {
            double xInput = posDrive.getMecanumX();
            double yInput = posDrive.getMecanumY();
            double rotationInput = wheelDrive.getMecanumRotation();

            setDriveStatus(xInput, yInput, rotationInput);
//            drive.mecanumDrive_Cartesian(xInput, yInput, rotationInput, gyro.getAngle()); //For mecanum wheeled robot.
            drive.arcadeDrive(yInput, rotationInput); //For non-mecanum wheeled robot. 
        } else {
            setDriveStatus(0, 0, 0);
        }
    }

    /**
     * Control robot via Z-Axis of hands and X-Axis of the skeleton. Thinking of
     * changing this to Z-Axis of head. This method is meant to flow in a way
     * that only 1 PIDController is on at a time. This method does not fully
     * utilize mecanum wheels for side to side movement.
     */
    public void kinectMindControl() { //TODO Modify this method so that the other 2 motors are moved also for mecanum wheels.
        if (kinect.getSkeleton().GetTrackState() != Skeleton.tTrackState.kNotTracked && isMostlyTracked()) {
            setDriveStatus(9, 9, 9);

            if (!mindPIDX.onTarget()) {
                if (!mindPIDX.isEnable()) {
                    mindPIDX.enable();
                }
                if (mindPIDZ.isEnable()) {
                    mindPIDZ.disable();
                    frontLeft.set(0);
                    rearLeft.set(0);
                }
                rearLeft.set(frontLeft.get());
            } else if (mindPIDX.onTarget()) {
                if (mindPIDX.isEnable()) {
                    mindPIDX.disable();
                    frontLeft.set(0);
                    rearLeft.set(0);
                }
                if (!mindPIDZ.onTarget()) {
                    if (!mindPIDZ.isEnable()) {
                        mindPIDZ.enable();
                    }
                    rearLeft.set(-frontLeft.get());
                } else { //Both axes are onTarget
                    mindPIDX.disable();
                    mindPIDZ.disable();
                }
            }
        } else {
            mindPIDX.disable();
            mindPIDZ.disable();
            setDriveStatus(9, 9, 9);
        }
    }

    /**
     * Determines if most of the Kinect skeleton is tracked or not. There are 20
     * joints to track. If 17/20 of them are tracked, then this will return
     * true. For this method, tracked will be considered if a joint is returning
     * the track state of tracked or inferred.
     *
     * @return Whether or not most body parts are tracked or not.
     */
    private boolean isMostlyTracked() {
        int trackCounter = 0;

        Skeleton.tJointTrackingState head = kinect.getSkeleton().GetHead().getTrackingState(),
                shoulderCenter = kinect.getSkeleton().GetShoulderCenter().getTrackingState(),
                spine = kinect.getSkeleton().GetSpine().getTrackingState(),
                hipCenter = kinect.getSkeleton().GetHipCenter().getTrackingState(),
                leftHand = kinect.getSkeleton().GetHandLeft().getTrackingState(),
                leftWrist = kinect.getSkeleton().GetWristLeft().getTrackingState(),
                leftElbow = kinect.getSkeleton().GetElbowLeft().getTrackingState(),
                leftShoulder = kinect.getSkeleton().GetShoulderLeft().getTrackingState(),
                rightHand = kinect.getSkeleton().GetHandRight().getTrackingState(),
                rightWrist = kinect.getSkeleton().GetWristRight().getTrackingState(),
                rightElbow = kinect.getSkeleton().GetElbowRight().getTrackingState(),
                rightShoulder = kinect.getSkeleton().GetShoulderRight().getTrackingState(),
                leftFoot = kinect.getSkeleton().GetFootLeft().getTrackingState(),
                leftAnkle = kinect.getSkeleton().GetAnkleLeft().getTrackingState(),
                leftKnee = kinect.getSkeleton().GetKneeLeft().getTrackingState(),
                leftHip = kinect.getSkeleton().GetHipLeft().getTrackingState(),
                rightFoot = kinect.getSkeleton().GetFootRight().getTrackingState(),
                rightAnkle = kinect.getSkeleton().GetAnkleRight().getTrackingState(),
                rightKnee = kinect.getSkeleton().GetKneeRight().getTrackingState(),
                rightHip = kinect.getSkeleton().GetHipRight().getTrackingState();

        if (isTrackedOrInferred(head)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(shoulderCenter)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(spine)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(hipCenter)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftHand)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftWrist)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftElbow)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftShoulder)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightHand)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightWrist)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightElbow)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightShoulder)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftFoot)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftAnkle)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftKnee)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(leftHip)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightFoot)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightAnkle)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightKnee)) {
            trackCounter++;
        }
        if (isTrackedOrInferred(rightHip)) {
            trackCounter++;
        }

        System.out.println(trackCounter);

        return (trackCounter >= 17);
    }

    private boolean isTrackedOrInferred(Skeleton.tJointTrackingState state) {
        return (state == Skeleton.tJointTrackingState.kTracked || state == Skeleton.tJointTrackingState.kInferred);
    }

    public void limitSwitchDrive() {
        double x = 0, y = 0, rotation = 0;

        if (forward.get() == PRESSED && backward.get() == PRESSED) {
            System.out.println("Forward/Backward movement error, both being pressed at same time");
            y = 0;
        } else if (forward.get() == PRESSED) {
            System.out.println("Forward Pressed");
            y = MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else if (backward.get() == PRESSED) {
            System.out.println("Backward Pressed");
            y = -MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else {
            rotation = 0;
        }

        if (right.get() == PRESSED && left.get() == PRESSED) {
            System.out.println("Strafe error, both being pressed at same time");
            x = 0;
        } else if (right.get() == PRESSED) {
            System.out.println("Right Pressed");
            x = MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else if (left.get() == PRESSED) {
            System.out.println("Left Pressed");
            x = -MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else {
            x = 0;
        }

        if (rotationLeft.get() == PRESSED && rotationRight.get() == PRESSED) {
            System.out.println("Rotation Error, both being pressed at same time");
            rotation = 0;
        } else if (rotationRight.get() == PRESSED) {
            System.out.println("Rotation Right Pressed");
            rotation = -MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else if (rotationLeft.get() == PRESSED) {
            System.out.println("Rotation Left Pressed");
            rotation = MAX_LIMIT_SWITCH_DRIVE_OUTPUT;
        } else {
            rotation = 0;
        }

        if (forward.get() == NOT_PRESSED && backward.get() == NOT_PRESSED && right.get() == NOT_PRESSED
                && left.get() == NOT_PRESSED && rotationLeft.get() == NOT_PRESSED && rotationRight.get() == NOT_PRESSED) {
            allPressed = false;
        } else {
            allPressed = true;
        }

        drive.mecanumDrive_Cartesian(x, y, rotation, rotation);
    }

    /**
     * Utilizes headingPID and controls all four wheels of the mecanum drive
     * also to stabilize the heading of the robot if it is moved while joysticks
     * are not moving the robot.
     */
    public void stabilizeHeading(double setpoint) {
        headingPID.setSetpoint(setpoint);
        drive.mecanumDrive_Cartesian(0, 0, -headingPID.get(), 0);
    }

    /**
     * Safety measure for switching between drive modes so that last PWM value
     * we wrote to it doesn't stick on.
     */
    public void stopAllMotors() {
        frontLeft.set(0);
        rearLeft.set(0);
        frontRight.set(0);
        rearRight.set(0);

        mindPIDX.disable();
        mindPIDZ.disable();

        smoothDrivePID.reset();
    }

    private void setDriveStatus(double x, double y, double rotation) {
        xSpeed = x;
        ySpeed = y;
        rotationSpeed = rotation;

        if (x > 0) {
            horizontalDriveStatus = "Driving Right";
        } else if (x < 0) {
            horizontalDriveStatus = "Driving Left";
        } else {
            horizontalDriveStatus = "No Side Movement";
        }

        if (y > 0) {
            verticalDriveStatus = "Driving Up";
        } else if (y < 0) {
            verticalDriveStatus = "Driving Down";
        } else {
            verticalDriveStatus = "No Up/Down Movement";
        }

        if (rotation > 0) {
            rotationStatus = "Rotating Clockwise";
        } else if (rotation < 0) {
            rotationStatus = "Rotating Counter-Clockwise";
        } else {
            rotationStatus = "No Rotation";
        }
    }

    public void testFrontLeft() {
        frontLeft.set(0.3);
    }

    public void testRearLeft() {
        rearLeft.set(0.3);
    }

    public void testFrontRight() {
        frontRight.set(0.3);
    }

    public void testRearRight() {
        rearRight.set(0.3);
    }

    public void joystickControlWithCorrection(double angle) {
        double xInput = 0, yInput = 0, rotationInput = 0;
        double rotationCorrection = 0; //Rotation adjustment given by PIDController.
        smoothDrivePID.setSetpoint(RecordStabilizeAngle.getStabilizeAngle());

        if (OI.getDriveX() >= 0.10) {
            xInput = OI.CoerceToRange(OI.getDriveX(), 0.10, 1, 0, MAX_DRIVE_OUTPUT);
        } else if (OI.getDriveX() <= -0.10) {
            xInput = OI.CoerceToRange(OI.getDriveX(), -1, -0.10, -MAX_DRIVE_OUTPUT, 0);
        }

        if (OI.getDriveY() >= 0.10) {
            yInput = OI.CoerceToRange(OI.getDriveY(), 0.10, 1, 0, MAX_DRIVE_OUTPUT);
        } else if (OI.getDriveY() <= -0.10) {
            yInput = OI.CoerceToRange(OI.getDriveY(), -1, -0.10, -MAX_DRIVE_OUTPUT, 0);
        }

        if (OI.getDriveRotation() >= 0.10) {
            rotationInput = OI.CoerceToRange(OI.getDriveRotation(), 0.10, 1, 0, MAX_DRIVE_OUTPUT);
        } else if (OI.getDriveRotation() <= -0.10) {
            rotationInput = OI.CoerceToRange(OI.getDriveRotation(), -1, -0.10, -MAX_DRIVE_OUTPUT, 0);
        }

        if (inRotationAxisThreshold()) {
            rotationCorrection = -smoothDrivePID.get();
        }

        RobotTemplate.rotationCorrection = rotationCorrection;

        drive.mecanumDrive_Cartesian(xInput, yInput, rotationInput, angle); //TEMP Taken out rotationCorrection
    }

    private boolean inXAxisThreshold() {
        return (OI.getDriveX() < 0.10 && OI.getDriveX() > -0.10);
    }

    private boolean inYAxisThreshold() {
        return (OI.getDriveY() < 0.10 && OI.getDriveY() > -0.10);
    }

    private boolean inRotationAxisThreshold() {
        return (OI.getDriveRotation() < 0.10 && OI.getDriveRotation() > -0.10);
    }
}
