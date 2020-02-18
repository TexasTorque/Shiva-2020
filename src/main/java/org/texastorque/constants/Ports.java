package org.texastorque.constants;

public class Ports {

    // talon CAN id
    public static final int FLYWHEEL_LEAD = 1; // right 
    public static final int FLYWHEEL_FOLLOW = 0; // left 

    // neo CAN id - DO NOT HAVE ID 0 IT DOES NOT WORK
    public static final int DB_LEFT_1 = 1;
    public static final int DB_LEFT_2 = 2;
    public static final int DB_RIGHT_1 = 3;
    public static final int DB_RIGHT_2 = 4;
    
    public static final int CLIMBER_LEFT = 11;
    public static final int CLIMBER_RIGHT = 12;

    // neo 550 CAN id - DO NOT HAVE ID 0 IT DOES NOT WORK
    public static final int INTAKE_ROTARY_LEFT = 5; // left side spark max 
    public static final int INTAKE_ROTARY_RIGHT = 6; // right side spark max 
    public static final int INTAKE_ROLLERS = 7;
    
    public static final int BELT_HIGH = 8;
    public static final int BELT_GATE = 9;
    //belt_gate is gate because they stole the climber SparkMax... don't know if it's temporary or not
    public static final int BELT_LOW = 11;

    public static final int SHOOTER_HOOD = 10;

    // testing individual motors 
    public static final int TALON_PORT = 0;
    public static final int SPARKMAX_PORT_1 = 1;
    public static final int SPARKMAX_PORT_2 = 2;

    // PWM
    public static final int CLIMB_SERVO_LEFT = 0;
    public static final int CLIMB_SERVO_RIGHT = 1;

    //Sensors
    public static final int MAG_SENSOR_HIGH = 0;
    public static final int MAG_SENSOR_LOW = 2;
} // ports 
