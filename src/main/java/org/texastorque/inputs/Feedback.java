package org.texastorque.inputs;
import edu.wpi.first.wpilibj.smartdashboard.*;
import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;
import org.texastorque.inputs.Input;
import edu.wpi.first.wpilibj.DigitalInput;
public class Feedback {

private static volatile Feedback instance;

TCS34725_I2C colorSensor;
// TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();

int init;

private int numOfColorCount = 0;
private String colorCounter;
private String newColorCounter;

private String targetColorString;

private Feedback(){
    colorSensor  = new TCS34725_I2C(false);
    try{
        colorSensor.enable();
    } catch (Exception e) {}
} // constructor

public void update(){
    colorSensorUpdate();
} // update 
 
public void colorSensorUpdate(){    
      newColorCounter = this.getColor();

        try{
            var values = colorSensor.getRawData();
            if(values.getR() >= 10200 & values.getR() < 10400 &
                values.getB() <= 6000 & values.getB() >= 5000 & 
                values.getG() > 10000 & values.getG() < 10400){
              SmartDashboard.putString("color", "yellow");
            }
            else if(values.getR() >= 1300 & values.getR() <= 2000 & 
                values.getB() <= 1000 & values.getB() > 0 &
                values.getG() <= 1000 & values.getG() > 0){
              SmartDashboard.putString("color", "red");
            }
            else if(values.getR() >= 800 & values.getR() <= 1500 & 
                    values.getB() >= 500 & values.getB() <= 1000 & 
                    values.getG() >= 800 & values.getG() <= 1500){
              SmartDashboard.putString("color", "green");
            }
            else if(values.getR() >= 900 & values.getR() <= 2000 & 
                    values.getB() >= 1000 & values.getB() <= 2000 & 
                    values.getG() >= 900 & values.getG() <= 1800){
              SmartDashboard.putString("color", "blue");
            }
            else{
              SmartDashboard.putString("color", "stop it rip");
            }
            
            System.out.println(colorSensor.getRawData());
          }
          catch(Exception e){}
              //loop for counting how many colors past?
            colorCounter = this.getColor();
        }

    // CREATE THE SMART DASHBOARD LIST CLICKER THING 
    /*
      Code for selective color choosing 
    */
    //Detect orig color
    //checks 
/*
    while(numOfColorCount <= 35){
       // TURN ON THE MOTOR
    }
    */

public boolean shouldGateRun(){
  if(Input.colorIsUp){
    if(!newColorCounter.equals(colorCounter)){
      numOfColorCount++;
      while(numOfColorCount <= 35){
        return true;
      }
    }
  }
  return false;
}
public boolean shouldRunToColor() {
  if(!targetColorString.equals(colorCounter)){
    return true;
  }else{
    return false;
  }
}

public String getColor(){
    try{
        var values = colorSensor.getRawData();
        if(values.getR() >= 10200 & values.getR() < 10400 &
            values.getB() <= 6000 & values.getB() >= 5000 & 
            values.getG() > 10000 & values.getG() < 10400){
          return "yellow";
        }
        else if(values.getR() >= 1300 & values.getR() <= 2000 & 
                values.getB() <= 1000 & values.getB() > 0 &
                values.getG() <= 1000 & values.getG() > 0){
          return "red";
        }
        else if(values.getR() >= 800 & values.getR() <= 1500 & 
                values.getB() >= 500 & values.getB() <= 1000 & 
                values.getG() >= 800 & values.getG() <= 1500){
          return "green";
        }
        else if(values.getR() >= 900 & values.getR() <= 2000 & 
                values.getB() >= 1000 & values.getB() <= 2000 & 
                values.getG() >= 900 & values.getG() <= 1800){
          return "blue";
        }
        else{
            return "undetectable";
        }
      }
      catch(Exception e){}
      return "Did not run thorugh";
}


public void smartDashboard(){
  SmartDashboard.putNumber("Target Color", 0);
  double targetColor = SmartDashboard.getNumber("targetColor", 0.0);
  if (targetColor == 0.0) {
    targetColorString = "red";
  } else if (targetColor == 0.1) {
    targetColorString = "blue";
  } else if (targetColor == 0.2) {
    targetColorString = "green";
  } else if (targetColor == 0.3) {
    targetColorString = "yellow";
  } else {
    targetColorString = "none";
  }
  SmartDashboard.putString("Chosen_TargetColor", targetColorString);
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