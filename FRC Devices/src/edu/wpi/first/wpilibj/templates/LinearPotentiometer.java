/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * A variable resistor/potentiometer that changes output depending on how much
 * you turn its shaft. This class is meant for linear potentiometers where the 
 * relationship between the amount of turns and the output voltage is linear. I
 * made this class to be meant for the potentiometers we have in our lab as of 
 * the 2012 FRC season, however, if you have different ones than what this code
 * may seem to give (because it can differ based on wiring and model) then 
 * hopefully this code may still be useful to you in making your own potentiometer
 * class. This also assumes that one turn in your potentiometer is 360 degrees and
 * 0 turns is considered to be when your potentiometer can no longer turn counterclockwise.
 * 
 * @author Eric
 */
public class LinearPotentiometer extends AnalogChannel {

    private double voltsPerTurn, distPerTurn = 0;
    
    /**
     * Create an instance of a LinearPotentiometer given the slot and channel.
     * Assumes a 5V power supply to the potentiometer.
     * 
     * @param slot The slot on the cRIO the Analog Module is plugged into.
     * @param channel The channel on the Analog Breakout the potentiometer is plugged into.
     * @param turns The number of turns the potentiometer is able to turn.
     */
    public LinearPotentiometer(final int slot, final int channel, final int turns) {
        super(slot, channel);
        voltsPerTurn = 5.0/turns;
    }

    /**
     * Create an instance of a LinearPotentiometer given the channel and assuming
     * the Analog Module is plugged into the default slot on the cRIO.
     * Assumes a 5V power supply to the potentiometer.
     * 
     * @param channel The channel on the Analog Breakout the potentiometer is plugged into.
     * @param turns The number of turns the potentiometer is able to turn.
     */
    public LinearPotentiometer(final int channel, final int turns) {
        this(getDefaultAnalogModule(), channel, turns);
    }
    
    /**
     * Set the distance reported by the getDistance() method per potentiometer turn.
     * This is meant to be used in conjunction with your robot subsystems. For
     * example, the distance traveled by an elevator on a robot. The distance
     * that gets referenced, meaning the position that will equal 0 distance is
     * initialized here.
     * 
     * @param distance The distance per turn.
     */
    public void setDistancePerTurn(double distance) { //TODO Implement a reference distance. Reference distance will be able to be reset by a reset method like an encoder.
        distPerTurn = distance;
    }
    
    /**
     * Obtain the distance as scaled by setDistancePerTurn. If setDistancePerTurn
     * was not called prior to calling this method, 0 will be returned.
     * 
     * @return 
     */
    public double getDistance() {
        return getTurns()*distPerTurn;
    }
    
    /**
     * Obtain the number of turns the potentiometer has turned. The getVoltage 
     * method of the AnalogChannel class is used to convert the voltage to 
     * number of turns as specified by the constructor.
     * 
     * @return The number of turns the potentiometer has turned from 0 turns.
     */
    public double getTurns() {
        return getVoltage()/voltsPerTurn;
    }
}
