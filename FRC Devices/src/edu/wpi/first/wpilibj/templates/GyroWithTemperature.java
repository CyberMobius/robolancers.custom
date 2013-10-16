/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;

/**
 * An add-on to the original gyro class to include temperature compensation and
 * and to simply read out the temperature on the gyro.
 * 
 * This gyro class is meant for the gyro that came in the 2012 FRC KOP (ADXRS652 Gyro). 
 * 
 * ABANDONED FOR NOW due to the opinion that our robots do not need the temperature
 * compensation because our robots only act under small periods of time. However
 * I do think this should be picked up again sometime in the future to be always 
 * prepared for unexpected events. There seems to be a lack of documentation in 
 * doing this now too.
 * 
 * @author Eric
 * 
 */
public class GyroWithTemperature extends Gyro {

    private final GyroTemperature temp;

    /**
     * Constructor to create an instance of a GyroWithTemperature given the slots and
     * the channels.
     * 
     * @param gyroSlot The slot on the cRIO the Analog Module is plugged into and
     * is used by the gyro.
     * @param gyroChannel The channel on the Analog Breakout the gyro is plugged into.
     * @param tempSlot The slot on the cRIo the Analog Module is plugged into and
     * is used by the temperature sensor on the gyro.
     * @param tempChannel The channel on the Analog Breakout the temperature sensor 
     * on the gyro is plugged into.
     */
    public GyroWithTemperature(final int gyroSlot, final int gyroChannel, final int tempSlot, final int tempChannel) {
        super(gyroSlot, gyroChannel);
        temp = new GyroTemperature(tempSlot, tempChannel);
    }

    /**
     * Constructor to create an instance of a GyroWithTemperature given the channel and
     * assuming the Analog Module is plugged into the default slot.
     * 
     * @param gyroChannel The channel on the Analog Breakout the gyro is plugged into.
     * @param tempChannel The channel on the Analog Breakout the temperature sensor 
     * on the gyro is plugged into.
     */
    public GyroWithTemperature(final int gyroChannel, final int tempChannel) {
        this(getDefaultAnalogModule(), gyroChannel, getDefaultAnalogModule(), tempChannel);
    }
    
    /**
     * Obtain the angle the gyro is reading with calibrations based on the 
     * temperature sensor on the gyro.
     * 
     */
    public double getAngleWithTempCalib() { //TODO Complete this method and find a better name
        return super.getAngle();
    }
}
