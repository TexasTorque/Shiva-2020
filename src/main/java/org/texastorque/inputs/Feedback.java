package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;

public class Feedback {

    private static volatile Feedback instance;

    TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();

    int init;

    private Feedback(){
        init = colorSensor.init();
    } // constructor

    public void update(){
        colorSensorUpdate();
    } // update 

    // ======== color sensor ========
    int red = 0;
    int green = 0;
    int blue = 0;
    int readTest;

    public void colorSensorUpdate(){
        SmartDashboard.putBoolean("initialized", colorSensor.sensorInitialized());
        readTest = colorSensor.readColors();
        // red = colorSensor.getRedVal();
        // green = colorSensor.getGreenVal();
        // blue = colorSensor.getBlueVal();
    }

    public void smartDashboard(){
        SmartDashboard.putNumber("red", red);
        SmartDashboard.putNumber("green", green);
        SmartDashboard.putNumber("blue", blue);
        SmartDashboard.putNumber("read", readTest);
        SmartDashboard.putNumber("init", init);
        
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
