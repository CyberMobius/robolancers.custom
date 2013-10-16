package edu.wpi.first.wpilibj.templates;

public class RobotMap {

    //Digital Sidecar PWM Channels (First Digital Module)
    public static final int MOTOR_FRONT_LEFT = 1,
            MOTOR_REAR_LEFT = 2,
            MOTOR_FRONT_RIGHT = 3,
            MOTOR_REAR_RIGHT = 4;
    //Digital Sidecar GPIO Channels (First Digital Module)
    public static final int LIMIT_LEFT = 1, 
            LIMIT_BACKWARD = 2,
            LIMIT_RIGHT = 3, 
            LIMIT_FORWARD = 4,
            LIMIT_ROTATION_LEFT = 5, 
            LIMIT_ROTATION_RIGHT = 6;
    //Digital Sidecar Relay Channels (First Digital Module)
    public static final int RELAY_LED_RED = 1,
            RELAY_LED_BLUE = 2,
            RELAY_AIR_LEFT = 3,
            RELAY_AIR_RIGHT = 4;
    //Analog Breakout Channels (First Analog Module)
    public static final int ANALOG_GYRO = 1;
    //Solenoid Breakout Channels (First Solenoid Module)
    public static final int SOLENOID_LEFT_VALVE = 1,
            SOLENOID_RIGHT_VALVE = 2;
}
