package org.texastorque.constants;

public class Constants {
    public static final double SECONDS_PER_MINUTE = 60;
    public static final double SECONDS_PER_SPARK_FEEDBACK = 0.1;

    public static final double TICKS_PER_ENCODER_REV = 4096;
    public static final double SHOOTER_REDUCTION = 2;
    public static final double RPM_VICTORSPX_CONVERSION = (TICKS_PER_ENCODER_REV * SHOOTER_REDUCTION) / (SECONDS_PER_MINUTE / SECONDS_PER_SPARK_FEEDBACK);
} // Constants 
