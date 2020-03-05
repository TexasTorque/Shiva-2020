package org.texastorque.inputs;
import edu.wpi.first.wpilibj.smartdashboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.texastorque.constants.*;
import org.texastorque.util.TCS34725ColorSensor;
import org.texastorque.util.TCS34725_I2C;

import edu.wpi.first.wpilibj.DriverStation;


public class Feedback {

private static volatile Feedback instance;

private String sentColor;

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
    shouldGateRun();
    shouldRunToColor();
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

public boolean shouldRunToColor(){
  sentColor = DriverStation.getInstance().getGameSpecificMessage();
  if(sentColor.length() > 0)
  {
    switch (sentColor.charAt(0))
    {
      case 'B' :
        if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
          return true;
        }
        break;
      case 'G' :
        if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
          return true;
        }
        break;
      case 'R' :
        if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
          return true;
        }
        break;
      case 'Y' :
        if(!sentColor.equals(colSeq.get(colSeq.size()-1))){
          return true;
        }
        break;
      default :
        return false;
    }
  }
  return false;
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

public void smartDashboard(){
  SmartDashboard.putNumber("NumOfColorCount", numOfColorCount);
  SmartDashboard.putString("SentColor", sentColor);
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