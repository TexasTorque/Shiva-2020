package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;

import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Feedback {

    private static volatile Feedback instance;
    private double targetArea;
    private static double hOffset;
    private double vOffset;
    
    // Conversions
    public final double DISTANCE_PER_PULSE = Math.PI * Constants.WHEEL_DIAMETER / Constants.PULSES_PER_ROTATION;
    public final double ANGLE_PER_PULSE = 360.0 / Constants.PULSES_PER_ROTATION;
    public final double LF_FEET_CONVERSION = Math.PI * (1.0/20) / Constants.PULSES_PER_ROTATION; // Using approximate shaft diameter
    public final double ULTRA_CONVERSION = 1.0 / 84;

    private final AHRS NX_gyro;

    // NetworkTables
    private NetworkTableInstance NT_instance;
    private NetworkTableEntry NT_offsetEntry;

    // Analog Input 
    // private AnalogInput mag_ultra = new AnalogInput(0);

    private Feedback(){
        NX_gyro = new AHRS(SPI.Port.kMXP);
    } // constructor

    public void update(){
        updateLimelight();
        updateNavX();
    } // update 

    // ======== limelight ========

    public void updateLimelight(){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);
        targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        hOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        vOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    public static double getXOffset(){
        return hOffset;
    }

    // ========== Gyro ==========

    private double NX_pitch;
    private double NX_yaw;
    private double NX_roll;

    public void resetNavX() {
        NX_gyro.reset();
    }

    public void updateNavX() {
        NX_pitch = NX_gyro.getPitch();
        NX_yaw = NX_gyro.getYaw();
        NX_roll = NX_gyro.getRoll();
    }

    public double getPitch() {
        return NX_pitch;
    }

    public double getYaw() {
        return NX_yaw;
    }

    public double getRoll() {
        return NX_roll;
    }

    public void zeroYaw() {
        NX_gyro.zeroYaw();
    }
 

    // ======== Other stuff =========
    
    public void smartDashboard(){
        SmartDashboard.putNumber("hOffset", hOffset);
    } // stuff to put in smart dashboard

    public static Feedback getInstance() {
        if (instance == null){
            synchronized (Feedback.class){
                if (instance == null)
                    instance = new Feedback();
            }
        }
        return instance;
    } // getInstance 
} // Feedback
