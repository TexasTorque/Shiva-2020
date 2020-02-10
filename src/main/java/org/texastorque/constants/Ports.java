package org.texastorque.constants;

public class Ports {

    // talon CAN id
    public static final int FLYWHEEL_LEAD = 1;
    public static final int FLYWHEEL_FOLLOW = 0;

    // neo CAN id - DO NOT HAVE ID 0 IT DOES NOT WORK
    public static final int DB_LEFT_1 = 1;
    public static final int DB_LEFT_2 = 2;
    public static final int DB_RIGHT_1 = 3;
    public static final int DB_RIGHT_2 = 4;
    
    public static final int CLIMBER1 = 1;
    public static final int CLIMBER2 = 2;

    // neo 550 CAN id - DO NOT HAVE ID 0 IT DOES NOT WORK
    public static final int INTAKE_ROTARY_LEAD = 5; // left side spark max 
    public static final int INTAKE_ROTARY_FOLLOW = 6; // right side spark max 
    public static final int INTAKE_ROLLERS = 7;
    
    public static final int BELT_LEAD = 8;
    public static final int BELT_FOLLOW = 9;

    public static final int SHOOTER_HOOD = 10;

    // testing individual motors 
    public static final int TALON_PORT = 0;
    public static final int SPARKMAX_PORT = 1;
    public static final int SPARKMAX_PORT_2 = 2;
} // ports 
