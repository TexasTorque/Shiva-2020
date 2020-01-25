/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.texastorque.constants;

/**
 * Add your docs here.
 */
public class Constants {
    public static final double SECONDS_PER_MINUTE = 60;
    public static final double SECONDS_PER_SPARK_FEEDBACK = 0.1;

    public static final double TICKS_PER_ENCODER_REV = 4096;
    public static final double SHOOTER_REDUCTION = 2;
    public static final double RPM_VICTORSPX_CONVERSION = (TICKS_PER_ENCODER_REV * SHOOTER_REDUCTION) / (SECONDS_PER_MINUTE / SECONDS_PER_SPARK_FEEDBACK);
}
