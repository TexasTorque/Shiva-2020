package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Feedback {

    private static volatile Feedback instance;
    private double targetArea;
    private static double hOffset;
    private double vOffset;
    
    private Feedback(){
    } // constructor

    public void update(){
        limelightUpdate();
    } // update 

    // ======== limelight ========

    public void limelightUpdate(){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(2);
        targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        hOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        vOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    }

    public static double getXOffset(){
        return hOffset;
    }

    // ======== Other stuff =========
    
    public void smartDashboard(){
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
