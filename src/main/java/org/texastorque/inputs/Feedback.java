package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import edu.wpi.first.wpilibj.DigitalInput;

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

    // ======== color sensor arduino =========

    // private ArrayList<DigitalInput> rgbPins = new ArrayList<DigitalInput>();
    private DigitalInput[] rgb = new DigitalInput[] {new DigitalInput(0), new DigitalInput(1), new DigitalInput(2)};
    private String colorDetected = "";

    public enum Color {
        NONE, YELLOW, RED, GREEN, BLUE;
    }
    private Color color = Color.NONE;

    public void updateColorSensor(){
        if (rgb[2].get()){
            color = Color.BLUE;
        }
        else if(!rgb[0].get() || rgb[1].get()){
            color = Color.NONE;
        } 
        else if(rgb[0].get() && rgb[1].get()){
            color = Color.GREEN;
        }
        else if(rgb[0].get()){
            color = Color.YELLOW;
        }
        else {
            color = Color.RED;
        }   
    } // update color sensor  

    // ======== Other stuff =========
    
    public void smartDashboard(){
        SmartDashboard.putNumber("red", red);
        SmartDashboard.putNumber("green", green);
        SmartDashboard.putNumber("blue", blue);
        SmartDashboard.putNumber("read", readTest);
        SmartDashboard.putNumber("init", init);
        SmartDashboard.putString("Color", color.toString());
        
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
