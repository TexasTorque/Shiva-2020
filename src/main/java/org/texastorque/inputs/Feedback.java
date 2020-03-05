package org.texastorque.inputs;
import edu.wpi.first.wpilibj.smartdashboard.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;
import edu.wpi.first.wpilibj.DigitalInput;
public class Feedback {

private static volatile Feedback instance;

TCS34725_I2C colorSensor;
// TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();

ArrayList<String> colSeq = new ArrayList<>();
String[] scripts = new String [] {"R","G","B","Y"};
int chosIndex;
int numOfColorCount = 0;
private String targetColorString;

private Feedback(){
    colorSensor  = new TCS34725_I2C(false);
    try{
        colorSensor.enable();
    } catch (Exception e) {}
} // constructor

public void update(){
    colorSensorUpdate();
    colorCompare();
} // update 
 
public void colorSensorUpdate(){    
        try{
            var values = colorSensor.getRawData();
            if(values.getR() >= 10200 & values.getR() < 10400 &
                values.getB() <= 6000 & values.getB() >= 5000 & 
                values.getG() > 10000 & values.getG() < 10400){
              SmartDashboard.putString("color", "yellow");
              colSeq.add("Y");
              chosIndex = 0;
            }
            else if(values.getR() >= 1300 & values.getR() <= 2000 & 
                    values.getB() <= 1000 & values.getB() > 0 &
                    values.getG() <= 1000 & values.getG() > 0){
              SmartDashboard.putString("color", "red");
              colSeq.add("R");
              chosIndex = 1;
            }
            else if(values.getR() >= 800 & values.getR() <= 1500 & 
                    values.getB() >= 500 & values.getB() <= 1000 & 
                    values.getG() >= 800 & values.getG() <= 1500){
              SmartDashboard.putString("color", "green");
              colSeq.add("G");
              chosIndex = 2;
            }
            else if(values.getR() >= 900 & values.getR() <= 2000 & 
                    values.getB() >= 1000 & values.getB() <= 2000 & 
                    values.getG() >= 900 & values.getG() <= 1800){
              SmartDashboard.putString("color", "blue");
              colSeq.add("B");
              chosIndex = 3;
            }
            else{
              SmartDashboard.putString("color", "stop it rip");
            }
            
            System.out.println(colorSensor.getRawData());
          }
          catch(Exception e){}
    }

public void colorCompare(){
  // while()
}

public boolean shouldGateRun(){
  if(Input.colorIsUp){
    if((colSeq.get(colSeq.size()-1)).equals(scripts[chosIndex])){
      numOfColorCount++;
      while(numOfColorCount <= 35){
        return true;
      }
    }
  }
  return false;
}

public boolean shouldRunToColor() {
  if(!targetColorString.equals(colSeq.get(colSeq.size()-1))){
    return true;
  }else{
    return false;
  }
}


public void smartDashboard(){
  SmartDashboard.putNumber("Target Color", 0);
  double targetColor = SmartDashboard.getNumber("targetColor", 0.0);
  if (targetColor == 0.0) {
    targetColorString = "R";
  } else if (targetColor == 0.1) {
    targetColorString = "B";
  } else if (targetColor == 0.2) {
    targetColorString = "G";
  } else if (targetColor == 0.3) {
    targetColorString = "Y";
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