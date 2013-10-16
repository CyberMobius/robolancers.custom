/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.kinect;

import edu.wpi.first.wpilibj.Kinect;
import edu.wpi.first.wpilibj.templates.OI;

/**
 *
 * @author Eric
 */
public class KinectSteeringWheelDrive {

    //TODO Perhaps implement gear shifting (Right hand to the ride side stick shift style) or LEDs w/ Kinect
    //When Kinect finds arm is stretched out to side, create a new thread and start checking if it does the proper Z coordinate changes...
    private final double //Y Coordinate on Kinect skeleton.
            THRESHOLD_WHEEL_DIAMETER = 1.50,
            THRESHOLD_Z_NEUTRAL_HANDS = 0.25, // +/- % constant of the human player's arm length to be considered in the neutral zone
            THRESHOLD_X_NEUTRAL_HANDS = 0.15,
            MOTOR_MAX_PID_SET = 0.75;
    private Kinect kinect;

    public KinectSteeringWheelDrive() {
        kinect = OI.kinect;
    }
    //Collect of temporary Strings to check the status of being in position... (All TEMP)
    public static String handHeights = "Pending", wheelDiameterS = "Pending";

    /**
     * Check if the player is in the correct position to start driving. This
     * will be true if the player's hand position is in a certain range relative
     * to their shoulders and hips.
     *
     * @return Whether or not the player is in the correct position to use
     * steering wheel drive.
     */
    public boolean isInPosition() {
        int positionCounter = 0; //Counts how many body parts pass the inspection //May or may not use this
        boolean inPosition = false;

        double handLeftX = kinect.getSkeleton().GetHandLeft().getX();
        double handLeftY = kinect.getSkeleton().GetHandLeft().getY();
        double handRightX = kinect.getSkeleton().GetHandRight().getX();
        double handRightY = kinect.getSkeleton().GetHandRight().getY();
        double shoulderCenterX = kinect.getSkeleton().GetShoulderCenter().getX();
        double kneeRightY = kinect.getSkeleton().GetKneeRight().getY();
        double kneeLeftY = kinect.getSkeleton().GetKneeLeft().getY();

        double shoulderWidth = Math.abs(kinect.getSkeleton().GetShoulderLeft().getX() - kinect.getSkeleton().GetShoulderRight().getX());
        double wheelDiameter = Math.abs(handLeftX - shoulderCenterX) + Math.abs(handRightX - shoulderCenterX); //Diameter of the wheel (A bit flawed because it only measures X Distance).

        if (handLeftY >= kneeLeftY && handRightY >= kneeRightY) {
            handHeights = "Hands Ready";
            positionCounter++;
        } else {
            handHeights = "Hand(s) Too Low or Too High";
        }

        //Does the player's hands that form a "steering wheel" within the corect diameter range? 
        //Are the player's hands too close or too far from each other?
        if (wheelDiameter <= (shoulderWidth + shoulderWidth * THRESHOLD_WHEEL_DIAMETER)) {
            positionCounter++;
            wheelDiameterS = "Wheel Ready";
        } else {
            wheelDiameterS = "Wheel Not Ready";
        }

        if (positionCounter == 2) {
            inPosition = true;
        } else {
            inPosition = false;
        }

        return inPosition;
    }

    /**
     * Based on the average Z-coordinates of the human player's hands.
     *
     * @return The Y value used in the mecanumDrive_Cartesian method in the
     * RobotDrive class
     */
    public double getMecanumY() { //TODO Redo with the angle between elbow, shoulder and hand z-axis or just scrap this now.
        double y = 0;

        if (isInPosition()) {
            //Relative distance on Z-Axis from hand to shoulder
            //TODO Take out absolute distance logic... Replace with previous logic...
            double shoulderCenterZ = kinect.getSkeleton().GetShoulderCenter().getZ();
            double shoulderToLeftHandZ = Math.abs(shoulderCenterZ - kinect.getSkeleton().GetHandLeft().getZ());
            double shoulderToRightHandZ = Math.abs(shoulderCenterZ - kinect.getSkeleton().GetHandRight().getZ());
            double handAverageZDistance = (shoulderToLeftHandZ + shoulderToRightHandZ) / 2;
            double neutral = shoulderCenterZ - OI.armLength / 2; //TODO Calibrate neutral zone of steering wheel (Z coordinate)

            //Neutral Y Movement zone will be halfway between arms all the way out and all the way in towards body, then +/- some of that
            if (handAverageZDistance <= (neutral + OI.armLength * THRESHOLD_Z_NEUTRAL_HANDS) && handAverageZDistance >= (neutral - OI.armLength * THRESHOLD_Z_NEUTRAL_HANDS)) {
                handAverageZDistance = (shoulderCenterZ + OI.armLength + shoulderCenterZ) / 2; //Will be thrown away by CoerceToRange method if hands are in the neutral zone.
            }

            y = -OI.CoerceToRange(handAverageZDistance, (shoulderCenterZ + OI.armLength),
                    shoulderCenterZ, -MOTOR_MAX_PID_SET, MOTOR_MAX_PID_SET);
            if (handAverageZDistance < (shoulderCenterZ + OI.armLength)) {
                y = MOTOR_MAX_PID_SET;
            } else if (handAverageZDistance > shoulderCenterZ) {
                y = -MOTOR_MAX_PID_SET;
            }
        } else {
            y = 0;
        }

        return y;
    }

    /**
     * Based on the X-coordinates of the human player's hands. This max amount
     * of rotation will be achieved when in the picture of the steering wheel,
     * one hand is at 12 o'clock and another hand is at 6 o'clock. If the right
     * hand is on top, rotation value will be negative. Inverse is true.
     *
     * @return The rotation value used in the mecanumDrive_Cartesian method in
     * the RobotDrive class
     */
    public double getMecanumRotation() {
        double rotation = 0;

        if (isInPosition()) {
            double handLeftX = kinect.getSkeleton().GetHandLeft().getX();
            double handLeftY = kinect.getSkeleton().GetHandLeft().getY();
            double handRightX = kinect.getSkeleton().GetHandRight().getX();
            double handRightY = kinect.getSkeleton().GetHandRight().getY();
            double shoulderCenterX = kinect.getSkeleton().GetShoulderCenter().getX();

            double leftHandNeutral = kinect.getSkeleton().GetShoulderLeft().getX();
            double rightHandNeutral = kinect.getSkeleton().GetShoulderRight().getX();

            //Find which hand is higher
            //Then track the X Coordinate of the hand that is higher. Adjust the rotation value based on that
            if (handLeftY > handRightY) { //Left hand is higher
                if (handLeftX > (leftHandNeutral - leftHandNeutral * THRESHOLD_X_NEUTRAL_HANDS) //If the human player's left hand is within the neutral zone...
                        && handLeftX < (leftHandNeutral + leftHandNeutral * THRESHOLD_X_NEUTRAL_HANDS)) {
                    handLeftX = (leftHandNeutral + leftHandNeutral * THRESHOLD_X_NEUTRAL_HANDS + shoulderCenterX) / 2; //Will be thrown away by CoerceToRange method if hands are in the neutral zone.
                }
                rotation = OI.CoerceToRange(handLeftX, (leftHandNeutral + leftHandNeutral * THRESHOLD_X_NEUTRAL_HANDS), shoulderCenterX, 0, MOTOR_MAX_PID_SET); //Turn right depending on X-Coordinate of Left Hand
                if (handLeftX > shoulderCenterX) {
                    rotation = MOTOR_MAX_PID_SET;
                }
            } else if (handRightY > handLeftY) {
                if (handRightX > (rightHandNeutral - rightHandNeutral * THRESHOLD_X_NEUTRAL_HANDS) //If the human player's right hand is within the neutral zone...
                        && handRightX < (rightHandNeutral + rightHandNeutral * THRESHOLD_X_NEUTRAL_HANDS)) {
                    handRightX = (rightHandNeutral + rightHandNeutral * THRESHOLD_X_NEUTRAL_HANDS + shoulderCenterX) / 2; //Will be thrown away by CoerceToRange method if hands are in the neutral zone.
                }
                rotation = OI.CoerceToRange(handRightX, shoulderCenterX, (rightHandNeutral - rightHandNeutral * THRESHOLD_X_NEUTRAL_HANDS), -MOTOR_MAX_PID_SET, 0); //Turn left depending on X-Coordinate of Right Hand
                if (handRightX < shoulderCenterX) {
                    rotation = -MOTOR_MAX_PID_SET;
                }
            } else {
                rotation = 0;
            }

        } else {
            rotation = 0;
        }

        return rotation;
    }
}