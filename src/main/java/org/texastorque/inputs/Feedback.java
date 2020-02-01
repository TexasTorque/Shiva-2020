package org.texastorque.inputs;

import org.texastorque.constants.*;
import edu.wpi.first.networktables.*;

public class Feedback {

    private static volatile Feedback instance;
    private double targetArea;
    private static double hOffset;
    private double vOffset;

    private Feedback(){

    } // constructor

    public void shooterMode(){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(2);
    }
    
    public void intakeMode(){
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);
    }

    public void update(){
        targetArea = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
        hOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
        vOffset = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
    } // update 

    public static double getHOffset(){
        return hOffset;
    }

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
