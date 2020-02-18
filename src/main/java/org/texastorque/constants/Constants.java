package org.texastorque.constants;

public class Constants {
    public static final double SECONDS_PER_MINUTE = 60;
    public static final double SECONDS_PER_SPARK_FEEDBACK = 0.1;

    public static final double CONVERSION_SHOOTER_TO_RPM = 32;
    
    public static final double TICKS_PER_ENCODER_REV = 4096;
    public static final double SHOOTER_REDUCTION = 2;
    public static final double RPM_VICTORSPX_CONVERSION = (TICKS_PER_ENCODER_REV * SHOOTER_REDUCTION) / (SECONDS_PER_MINUTE / SECONDS_PER_SPARK_FEEDBACK);

    public static final double DB_WIDTH = 2.0; // (ft/s)
    public static final double WHEEL_DIAMETER = 0.5; // (ft)
    public static final double PULSES_PER_ROTATION = 1000;

    public static final double DB_LOW_MAX_SPEED = 8.0; // Low gear max speed (ft/s)
    public static final double DB_LOW_MAX_ACCEL = 6.0; // Low gear max acceleration (ft/s/s)
    public static final double DB_LOW_MAX_JERK = 60.0; // Low gear max jerk (ft/s/s/s)

    public static final double DB_HIGH_MAX_SPEED = 15.0;
    public static final double DB_HIGH_MAX_ACCEL = 5.0;
    public static final double DB_HIGH_MAX_JERK = 60.0;

    public static final double CAMERA_ANGLE_X = 60;
} // Constants 