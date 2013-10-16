/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;

/**
 * The temperature sensor on the gyro which was given in the 2012 FRC KOP (ADXRS652 Gyro)
 * This class is used in conjunction with the GyroWithTemperature class to use 
 * temperature compensation for gyro noise. Can also be used as a regular 
 * temperature sensor.
 * 
 * @author Eric
 */
public class GyroTemperature extends AnalogChannel {

    /**
     * Units of temperature
     */
    public static class TemperatureUnits {

        /**
         * The integer value representing this enumeration.
         */
        public final int value;
        static final int CELSIUS_VAL = 0;
        static final int FAHRENHEIT_VAL = 1;
        static final int KELVINS_VAL = 2;
        /** Celsius Temperature Units. */
        public static final TemperatureUnits CELSIUS = new TemperatureUnits(CELSIUS_VAL);
        /** Fahrenheit Temperature Units. */
        public static final TemperatureUnits FAHRENHEIT = new TemperatureUnits(FAHRENHEIT_VAL);
        /** Kelvin Temperature Units. */
        public static final TemperatureUnits KELVINS = new TemperatureUnits(KELVINS_VAL);

        private TemperatureUnits(int value) {
            this.value = value;
        }
    }

    /**
     * Create an instance of the temperature sensor on the gyro given the channel
     * and assuming the Analog Module is plugged into the default slot.
     * 
     * @param channel The channel the temperature sensor on the gyro is plugged
     * into on the Analog Breakout
     */
    public GyroTemperature(int channel, TemperatureUnits units) {
        super(channel);
    }

    /**
     * Create an instance of the temperature on the gyro given the slot and channel.
     * 
     * @param slot The slot on the cRIO the Analog Module is plugged into.
     * @param channel The channel the temperature sensor on the gyro is plugged
     * into on the Analog Breakout
     */
    public GyroTemperature(int slot, int channel) {
        super(slot, channel);
    }

    /**
     * Obtain the temperature that the gyro is sensing. This uses the data from the
     * ADXRS652 Gyro datasheet, that at 25 Degrees Celsius, the gyro will report 
     * 2.5V plus 9mV/(1 Degree Celsius). This method converts the getVoltage()
     * method into its units.
     * 
     * @param units The units of temperature you want to be returned.
     * 
     * @return The temperature the gyro is reporting in the units specified.
     */
    public double getTemperature(TemperatureUnits units) {
        double temp;
        double changeInTemp; //Change in temperature from 25 Degrees Celsius.

        changeInTemp = ((getVoltage() - 2.5) * 1000.0) / 9.5;
        temp = changeInTemp + 25; //Temperature in Celsius.

        if (units == TemperatureUnits.FAHRENHEIT) {
            temp = temp * 9.0 / 5.0 + 32; //Convert the temperature in Celsius to Fahrenheit
        } else if (units == TemperatureUnits.KELVINS) {
            temp = temp + 273.15; //Convert the temperature in Celsius to Kelvins
        }

        return temp;
    }

    /**
     * Obtain the temperature that the gyro is sensing. This uses the data from the
     * ADXRS652 Gyro datasheet, that at 25 Degrees Celsius, the gyro will report 
     * 2.5V plus 9mV/(1 Degree Celsius). This method converts the getAverageVoltage()
     * method into its units.
     * 
     * @param units The units of temperature you want to be returned.
     * 
     * @return The temperature the gyro is reporting in the units specified.
     */
    public double getAverageTemperature(TemperatureUnits units) {
        double temp;
        double changeInTemp; //Change in temperature from 25 Degrees Celsius.

        changeInTemp = ((getAverageVoltage() - 2.5) * 1000.0) / 9.5;
        temp = changeInTemp + 25; //Temperature in Celsius.

        if (units == TemperatureUnits.FAHRENHEIT) {
            temp = temp * 9.0 / 5.0 + 32; //Convert the temperature in Celsius to Fahrenheit
        } else if (units == TemperatureUnits.KELVINS) {
            temp = temp + 273.15; //Convert the temperature in Celsius to Kelvins
        }

        return temp;
    }
}
