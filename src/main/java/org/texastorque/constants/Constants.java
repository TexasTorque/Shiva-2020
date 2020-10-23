package org.texastorque.constants;

public class Constants {
    public static final double SECONDS_PER_MINUTE = 60;
    public static final double SECONDS_PER_SPARK_FEEDBACK = 0.1;

    public static final double CONVERSION_SHOOTER_TO_RPM = 32;
    
    public static final double TICKS_PER_ENCODER_REV = 4096;
    public static final double SHOOTER_REDUCTION = (2/3);
    public static final double RPM_NEO_SPARKMAX_CONVERSION = (TICKS_PER_ENCODER_REV * SHOOTER_REDUCTION) / (SECONDS_PER_MINUTE / SECONDS_PER_SPARK_FEEDBACK);

    public static final double DB_WIDTH = 2.29; // (ft)
    public static final double WHEEL_DIAMETER = 0.5; // (ft)
    public static final double PULSES_PER_ROTATION = 1000;

    public static final double DB_MAX_SPEED = 15.0; // max speed (ft/s)
    public static final double DB_MAX_ACCEL = 5.0; // max acceleration (ft/s/s)
    public static final double DB_MAX_JERK = 60.0; // max jerk (ft/s/s/s)

    public static final double TICKS_PER_FOOT_DB = 4.4096;
    public static final double CAMERA_ANGLE_X = 60;

    public static final double DIFFERENCE_CENTERPORT_LIMELIGHT = 75.25;
    public static final double LIMELIGHT_ANGLE_OFFSET = 27;
} // Constants 