package org.texastorque;

// ========= imports ======== 
import org.texastorque.subsystems.*;
import org.texastorque.inputs.*;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.base.TorqueIterative;
import org.texastorque.constants.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

import java.util.ArrayList;


import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

// =============================== Robot ===============================

public class Robot extends TorqueIterative {

  // make instances of subsystems to later place into arraylist 
  private ArrayList<Subsystem> subsystems;
  private Subsystem driveBase = DriveBase.getInstance();
  private Subsystem shooter = Shooter.getInstance();
  
  // make instances of other useful classes 
  private State state = State.getInstance();
  private Input input = Input.getInstance();
  private Feedback feedback = Feedback.getInstance();

  TalonSRX talonA;
  TalonSRX talonB;
  // ======= initialize ============
  public void robotInit() {
    initSubsystems();  
    // talonA = new TalonSRX(Ports.FLYWHEEL_A);
    // talonB = new TalonSRX(Ports.FLYWHEEL_B);
    // talonB.selectProfileSlot(0, 0);
    // talonB.set(ControlMode.Velocity, 0);  
    // talonA.set(ControlMode.Follower,Ports.FLYWHEEL_B);
  } // initialize robot

  public void initSubsystems(){
    subsystems = new ArrayList<Subsystem>();
    // subsystems.add(driveBase);
    subsystems.add(shooter);
  } // initialize subsystems 

  @Override
  public void autoInit(){
    for (Subsystem system : subsystems){
      system.autoInit();
    }
  } // initialize in auto

  @Override
  public void teleopInit(){
    state.setRobotState(RobotState.TELEOP);
    for (Subsystem system : subsystems){
      system.teleopInit();
    }
  } // initialize in teleop

  @Override
  public void disabledInit(){
    for (Subsystem system : subsystems){
      system.disabledInit();
    }
  } // initialize when disabled

  // ======== continous ==============
  public void autoContinous(){
    for (Subsystem system : subsystems){
      system.run(state.getRobotState());
    }
  } // do continously in autonomous

  @Override
  public void teleopContinuous(){
    input.updateControllers();
    for (Subsystem system : subsystems){
      system.run(state.getRobotState());
    }
//trench 47800, 44800
    // talonB.set(ControlMode.Velocity, 7000*Constants.conversion_Shooter);
    // talonA.set(ControlMode.Follower, 1);
  } // do continuously in teleop

  @Override
  public void disabledContinuous(){
    for (Subsystem system : subsystems){
      system.disabledContinuous();
    }
  } // do continuously when disabled 

  @Override
  public void alwaysContinuous(){
    feedback.update();
    feedback.smartDashboard();

    for (Subsystem system : subsystems){
      system.disabledContinuous();
    }
  } // do continously always

  // ========== others ===========
  @Override 
  public void endCompetition(){
  } // endCompetition

} // Robot