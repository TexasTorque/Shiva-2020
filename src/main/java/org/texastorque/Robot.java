package org.texastorque;

// ========= imports ======== 
// import org.texastorque.subsystems.*;
// import org.texastorque.inputs.*;

import org.texastorque.torquelib.base.TorqueIterative;
import org.texastorque.torquelib.util.GenericController;
import org.texastorque.torquelib.component.TorqueMotor;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.ArrayList;

// ===================================== Robot ===============================

public class Robot extends TorqueIterative {

  // private ArrayList<Subsystem> subsystems;
  // private Subsystem driveBase = DriveBase.getInstance();
  // private Subsystem rotary = Rotary.getInstance();

  private GenericController driver;
  
  private TorqueMotor left1;
  private TorqueMotor left2;
  private TorqueMotor left3;
  private TorqueMotor right1;
  private TorqueMotor right2;
  private TorqueMotor right3;

  private double leftSpeed = 0.0;
  private double rightSpeed = 0.0;

  private boolean clockwise = true;

  // ======= initialize ============
  public void robotInit() {
    driver = new GenericController(0,0.1);
    left1 = new TorqueMotor(new VictorSP(0), !clockwise);
    left2 = new TorqueMotor(new VictorSP(1), !clockwise);
    left3 = new TorqueMotor(new VictorSP(2), !clockwise);
    
    right1 = new TorqueMotor(new VictorSP(3), clockwise);
    right2 = new TorqueMotor(new VictorSP(4), clockwise);
    right3 = new TorqueMotor(new VictorSP(5), clockwise);
  } // initialize robot

  public void initSubsystems(){
    leftSpeed = 0;
    rightSpeed = 0;
  } // initialize subsystems 

  @Override
  public void autoInit(){
    leftSpeed = 0;
    rightSpeed = 0;
  } // initialize in auto

  @Override
  public void teleopInit(){
    leftSpeed = 0;
    rightSpeed = 0;
  } // initialize in teleop

  @Override
  public void disabledInit(){
    leftSpeed = 0;
    rightSpeed = 0;
  } // initialize when disabled

  // ======== continous ==============
  public void autoContinous(){
    leftSpeed = 0;
    rightSpeed = 0;
  } // do continously in autonomous

  @Override
  public void teleopContinuous(){
    leftSpeed = - driver.getLeftYAxis() + 0.4*Math.pow(driver.getRightXAxis(),2)*Math.signum(driver.getRightXAxis());
    rightSpeed = - driver.getLeftYAxis() - 0.4*Math.pow(driver.getRightXAxis(),2)*Math.signum(driver.getRightXAxis());

    left1.set(leftSpeed);
    left2.set(leftSpeed);
    left3.set(leftSpeed);
    right1.set(rightSpeed);
    right2.set(rightSpeed);
    right3.set(rightSpeed);
  } // do continuously in teleop

  @Override
  public void disabledContinuous(){
  } // do continuously when disabled 

  @Override
  public void alwaysContinuous(){
  } // do continously always

  // ========== others ===========
  @Override 
  public void endCompetition(){
  } // endCompetition

} // Robot
