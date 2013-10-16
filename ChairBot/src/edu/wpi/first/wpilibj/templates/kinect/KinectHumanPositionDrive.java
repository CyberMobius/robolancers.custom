/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.kinect;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.templates.OI;

/**
 * The player's field of movement will vary based on the coordinates given by
 * the method kinect.getPosition().
 *
 * @author Eric
 */
public class KinectHumanPositionDrive {
    //TODO Might calibrate a smaller neutral box, or just expand the space we'll get, so to do that, expand the inputMax in CoerceToRange methods.
    //Box thresholds are in meters (as told by the Kinect).

    private final double THRESHOLD_ROTATION = 25, //Threshold for shoulder angle rotation.
            //            THRESHOLD_BOX = 0.1524, //Distance in addition to the outer box to prevent sudden robot stoppings while human is still in the box.
            THRESHOLD_BOX = 0.5, //Changed for larger distances because box will be larger at PRX.
            THRESHOLD_LEFT_BOX = -0.9144 - THRESHOLD_BOX, //Actual is -0.9144 but this is extended a bit further so the robot won't stop at the edge of the square.
            THRESHOLD_RIGHT_BOX = 0.9144 + THRESHOLD_BOX, //Actual is 0.9144 but this is extended a bit further so the robot won't stop at the edge of the square.
            THRESHOLD_TOP_BOX = 1.52400 - THRESHOLD_BOX, //Actual is 1.52400 but this is extended a bit further so the robot won't stop at the edge of the square.
            THRESHOLD_BOTTOM_BOX = 3.3528 + THRESHOLD_BOX, //Actual is 3.3528 but this is extended a bit further so the robot won't stop at the edge of the square.
            THRESHOLD_INNER_BOX = 0.1016, //Distance in addition to the inner box to prevent robot from robot from accidentally moving while in the inner box.
            THRESHOLD_LEFT_NEUTRAL_BOX = -0.4572 - THRESHOLD_INNER_BOX, //Actual is -0.4572 but this is increased to reduce accidents.
            THRESHOLD_RIGHT_NEUTRAL_BOX = 0.4572 + THRESHOLD_INNER_BOX, //Actual is 0.4572 but this is increased to reduce accidents.
            THRESHOLD_TOP_NEUTRAL_BOX = 1.9812 - THRESHOLD_INNER_BOX, //Actual is 1.9812 but this is increased to reduce accidents
            THRESHOLD_BOTTOM_NEUTRAL_BOX = 2.8956 + THRESHOLD_INNER_BOX, //Actual is 2.8956 but this is increased to reduce accidents
            MOTOR_MAX_PID_SET = 0.75;
    private final Kinect kinect;

    public KinectHumanPositionDrive() {
        kinect = OI.kinect;
    }

    /**
     * Gives the value based on the X coordinate of the human player
     *
     * @return The X value to be used in the mecanumDrive_Cartesian method of
     * the RobotDrive class
     */
    public double getMecanumX() {
        double x = 0;

        double positionX = kinect.getPosition().getX();

        //If kinect.getPosition().getX() is within the neutral box...
        if (positionX > THRESHOLD_LEFT_NEUTRAL_BOX && positionX < THRESHOLD_RIGHT_NEUTRAL_BOX) {
            x = 0; //Create a value that'll make the X Speed 0.
        } else {
            x = OI.CoerceToRange(positionX, THRESHOLD_LEFT_BOX, THRESHOLD_RIGHT_BOX, -MOTOR_MAX_PID_SET, MOTOR_MAX_PID_SET);
            /*
             * if (positionX > THRESHOLD_RIGHT_BOX) { x = MOTOR_MAX_PID_SET; }
             * else if (positionX < THRESHOLD_LEFT_BOX) { x =
             * -MOTOR_MAX_PID_SET; }
             */
        }

        return x;
    }

    /**
     * Gives the value based on the Z coordinate of the human player
     *
     * @return The Y value to be used in the mecanumDrive_Cartesian method of
     * the RobotDrive class
     */
    public double getMecanumY() {
        double y = 0;

        double positionZ = kinect.getPosition().getZ();
        //If kinect.getPosition().getZ() is within neutral the box...
        if (positionZ > THRESHOLD_TOP_NEUTRAL_BOX && positionZ < THRESHOLD_BOTTOM_NEUTRAL_BOX) {
            y = 0; //Create a value that'll make the Y Speed 0.
        } else if (positionZ <= 1) { //Precaution
            y = 0;
        } else {
            y = -OI.CoerceToRange(positionZ, THRESHOLD_TOP_BOX, THRESHOLD_BOTTOM_BOX, -MOTOR_MAX_PID_SET, MOTOR_MAX_PID_SET);
            /*
             * if (positionZ < 0) { //Calibrate as parameters for CoerceToRange
             * are changed y = MOTOR_MAX_PID_SET; } else if (positionZ > 10) { y
             * = -MOTOR_MAX_PID_SET; }
             */
        }
        return y;
    }

    /**
     * Gives the value based on the X coordinates of the human player's
     * shoulders
     *
     * @return The rotation value to be used in the mecanumDrive_Cartesian
     * method of the RobotDrive class
     */
    public double getShoulderRotation() {
        double rotation = 0;

        double denom;

        double shoulderLeftX = kinect.getSkeleton().GetShoulderLeft().getX();
        double shoulderLeftZ = kinect.getSkeleton().GetShoulderLeft().getZ();
        double shoulderRightX = kinect.getSkeleton().GetShoulderRight().getX();
        double shoulderRightZ = kinect.getSkeleton().GetShoulderRight().getZ();
        double shoulderCenterX = kinect.getSkeleton().GetShoulderCenter().getX();
        double shoulderCenterZ = kinect.getSkeleton().GetShoulderCenter().getZ();

        double shoulderLeftRotation = 0;
        double shoulderRightRotation = 0;

        /*
         * TODO Might wanna change the calculation of angle so that on the
         * overhead polar coordinate axes of the Kinect Skeleton (Origin being
         * shoulder center), the angle calculated will equal 0 degrees when
         * (theta = 90). That way, unlike the current system, angles don't jump
         * between +/- 90.
         */
        denom = shoulderCenterX - shoulderLeftX;
        if (denom != 0) {
            shoulderLeftRotation = Math.toDegrees(MathUtils.atan((shoulderCenterZ - shoulderLeftZ) / denom)); //Angle of rotation
        }

        denom = shoulderRightX - shoulderCenterX;
        if (denom != 0) {
            shoulderRightRotation = Math.toDegrees(MathUtils.atan((shoulderCenterZ - shoulderRightZ) / denom)); //Angle of rotation
        }

        if (shoulderRightRotation > THRESHOLD_ROTATION || shoulderLeftRotation > THRESHOLD_ROTATION) { //If either shoulder has rotated enough...
            if (shoulderRightZ < shoulderLeftZ) { //If your right shoulder is closer to the Kinect...
                rotation = OI.CoerceToRange(shoulderRightRotation, THRESHOLD_ROTATION, 90, -MOTOR_MAX_PID_SET, 0); //Then turn left depending on magnitude of rotation.
                if (shoulderRightRotation < 0) {
                    rotation = -MOTOR_MAX_PID_SET;
                }
            } else if (shoulderLeftZ < shoulderRightZ) { //If your left shoulder is closer to the Kinect...
                rotation = -OI.CoerceToRange(shoulderLeftRotation, THRESHOLD_ROTATION, 90, -MOTOR_MAX_PID_SET, 0); //Then turn right depending on magnitude of rotation.
                if (shoulderLeftRotation < 0) {
                    rotation = MOTOR_MAX_PID_SET;
                }
            }
        } else {
            rotation = 0;
        }

        return rotation;
    }

    public double getArmRotation() {
        double rotation = 0;

        double handLeftY = kinect.getSkeleton().GetHandLeft().getY();
        double handRightY = kinect.getSkeleton().GetHandRight().getY();
        double headY = kinect.getSkeleton().GetHead().getY();
        double hipCenterY = kinect.getSkeleton().GetHipCenter().getY();

        if ((handLeftY > hipCenterY) && (handRightY > hipCenterY) && (handLeftY < headY) && (handRightY < headY)) {
            double xLeftAngle = OI.getLeftX();
            double xRightAngle = OI.getRightX();

            /*
             * Scenarios (Which only will work if your arms are above the hips
             * (left and right) and below the head):
             *
             * 1. Right arm forward or at 0, left arm backward or at 0
             * //Equation
             *
             * 2. Right arm backward or at 0, left arm forward or at 0
             * //Equation
             *
             * 3. Right arm forward AND left arm forward.
             *
             * 4. Right arm backward AND right arm backward.
             */

            if ((xLeftAngle >= 0 && xRightAngle <= 0) || (xLeftAngle <= 0 && xRightAngle >= 0)) { //Scenario 1 through 2.
                rotation = (-xRightAngle + xLeftAngle) / 2;
            } else if ((xRightAngle > 0 && xLeftAngle > 0) || (xRightAngle < 0 && xLeftAngle < 0)) { //Scenario 3 through 4.
                rotation = 0;
            }
        }

        return rotation;
    }
}
