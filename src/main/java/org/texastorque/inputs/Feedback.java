package org.texastorque.inputs;

import org.texastorque.constants.*;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Feedback {

    private static volatile Feedback instance;
    private DigitalInput rgb0 = new DigitalInput(0);
    private DigitalInput rgb1 = new DigitalInput(1);
    private DigitalInput rgb2 = new DigitalInput(2);
    private String color = "";
    private Feedback(){

    } // constructor

    public void update(){
        if(rgb2.get()){
            color = "Blue";
        }
        else if(!(rgb0.get()||rgb1.get())){
            color = "None";
        }
        else if(rgb0.get() && rgb1.get()){
            color = "Green";
        }
        else if(rgb0.get()){
            color = "Yellow";
        }
        else{
            color = "Red";
        }

    } // update 

    public void smartDashboard(){
        SmartDashboard.putBoolean("RGB0", rgb0.get());
        SmartDashboard.putBoolean("RGB1", rgb1.get());
        SmartDashboard.putBoolean("RGB2", rgb2.get());
        SmartDashboard.putString("Color", color);
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
