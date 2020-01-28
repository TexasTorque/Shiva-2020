package org.texastorque.inputs;

import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;
import edu.wpi.first.wpilibj.DigitalInput;

public class Feedback {

    private static volatile Feedback instance;

    // TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();
    // TCS34725_I2C colorSensor;

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
    //     SmartDashboard.putNumber("yellow", 0);
    //     SmartDashboard.putNumber("red", 0);
    //     SmartDashboard.putNumber("green", 0);
    //     SmartDashboard.putNumber("blue", 0);
    //     try{
    //         var values = colorSensor.getRawData();
    //         if(values.getR() >= 2000 & values.getB() <= 1000 & values.getG() > 1000){
    //           SmartDashboard.putNumber("yellow", 1);
    //         }
    //         else if(values.getR() >= 1300 & values.getR() <= 2000 & 
    //                 values.getB() <= 1000 & 
    //                 values.getG() <= 1000){
    //           SmartDashboard.putNumber("red", 1);
    //         }
    //         else if(values.getR() >= 800 & values.getR() <= 1500 & 
    //                 values.getB() >= 500 & values.getB() <= 1000 & 
    //                 values.getG() >= 800 & values.getG() <= 1500){
    //           SmartDashboard.putNumber("green", 1);
    //         }
    //         else if(values.getR() >= 900 & values.getR() <= 2000 & 
    //                 values.getB() >= 1000 & values.getB() <= 2000 & 
    //                 values.getG() >= 900 & values.getG() <= 1800){
    //           SmartDashboard.putNumber("blue", 1);
    //         }
    //         else{
    //           SmartDashboard.putNumber("stop it rip", 1);
    //         }
            
    //         System.out.println(colorSensor.getRawData());
    //       }
    //       catch(Exception e){}
    // }

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
        // SmartDashboard.putNumber("red", red);
        // SmartDashboard.putNumber("green", green);
        // SmartDashboard.putNumber("blue", blue);
        // SmartDashboard.putNumber("read", readTest);
        // SmartDashboard.putNumber("init", init);
        // SmartDashboard.putString("Color", color.toString());
        
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
