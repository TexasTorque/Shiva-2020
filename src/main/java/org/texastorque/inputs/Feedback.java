package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;
import edu.wpi.first.wpilibj.DigitalInput;

public class Feedback {

    private static volatile Feedback instance;

    // TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();
    TCS34725_I2C colorSensor;

    int init;

    private Feedback(){
        // colorSensor  = new TCS34725_I2C(false);
        // try{
        //     colorSensor.enable();
        // } catch (Exception e) {}
    } // constructor

    public void update(){
        // colorSensorUpdate();
    } // update 

    // ======== color sensor ========

    // public void colorSensorUpdate(){
    //     try{
    //         var values = colorSensor.getRawData();
    //         if(values.getR() >= 10200 & values.getR() < 10400 &
    //             values.getB() <= 6000 & values.getB() >= 5000 & 
    //             values.getG() > 10000 & values.getG() < 10400){
    //           SmartDashboard.putString("color", "yellow");
    //         }
    //         else if(values.getR() >= 1300 & values.getR() <= 2000 & 
    //                 values.getB() <= 1000 & values.getB() > 0 &
    //                 values.getG() <= 1000 & values.getG() > 0){
    //           SmartDashboard.putString("color", "red");
    //         }
    //         else if(values.getR() >= 800 & values.getR() <= 1500 & 
    //                 values.getB() >= 500 & values.getB() <= 1000 & 
    //                 values.getG() >= 800 & values.getG() <= 1500){
    //           SmartDashboard.putString("color", "green");
    //         }
    //         else if(values.getR() >= 900 & values.getR() <= 2000 & 
    //                 values.getB() >= 1000 & values.getB() <= 2000 & 
    //                 values.getG() >= 900 & values.getG() <= 1800){
    //           SmartDashboard.putString("color", "blue");
    //         }
    //         else{
    //           SmartDashboard.putString("color", "stop it rip");
    //         }
            
    //         System.out.println(colorSensor.getRawData());
    //       }
    //       catch(Exception e){}
    // }

    // // ======== color sensor arduino =========

    // // private ArrayList<DigitalInput> rgbPins = new ArrayList<DigitalInput>();
    // private DigitalInput[] rgb = new DigitalInput[] {new DigitalInput(0), new DigitalInput(1), new DigitalInput(2)};
    // private String colorDetected = "";

    // public enum Color {
    //     NONE, YELLOW, RED, GREEN, BLUE;
    // }
    // private Color color = Color.NONE;

    // public void updateColorSensor(){
    //     if (rgb[2].get()){
    //         color = Color.BLUE;
    //     }
    //     else if(!rgb[0].get() || rgb[1].get()){
    //         color = Color.NONE;
    //     } 
    //     else if(rgb[0].get() && rgb[1].get()){
    //         color = Color.GREEN;
    //     }
    //     else if(rgb[0].get()){
    //         color = Color.YELLOW;
    //     }
    //     else {
    //         color = Color.RED;
    //     }   
    // } // update color sensor  

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
