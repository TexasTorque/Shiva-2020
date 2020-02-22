package org.texastorque;

// ========= imports ======== 
import org.texastorque.subsystems.*;
import org.texastorque.auto.AutoManager;
import org.texastorque.inputs.*;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.base.TorqueIterative;
import org.texastorque.constants.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;


import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

// =============================== Robot ===============================

public class Robot extends TorqueIterative {

  // make instances of subsystems to later place into arraylist 
  private ArrayList<Subsystem> subsystems;
  private Subsystem driveBase = DriveBase.getInstance();
  // private Subsystem shooter = Shooter.getInstance();
  // private Subsystem climber = Climber.getInstance();
  // private Subsystem intake = Intake.getInstance();
  // private Subsystem magazine = Magazine.getInstance();
  // private Subsystem testMotors = TestMotors.getInstance();
  
  // make instances of other useful classes 
  private State state = State.getInstance();
  private Input input = Input.getInstance();
  private Feedback feedback = Feedback.getInstance();
  private AutoManager autoManager;

  TalonSRX talonA;
  TalonSRX talonB;
  // ======= initialize ============
  public void robotInit() {
    initSubsystems();    
  } // initialize robot

  public void initSubsystems(){
    subsystems = new ArrayList<Subsystem>();
    subsystems.add(driveBase);
    // subsystems.add(shooter);
    // subsystems.add(climber);
    // subsystems.add(intake);
    // subsystems.add(magazine);
    autoManager = AutoManager.getInstance();
    // subsystems.add(testMotors);
  } // initialize subsystems 

  @Override
  public void autoInit(){
    // autoManager = AutoManager.getInstance();
    state.setRobotState(RobotState.AUTO);
    autoManager.chooseSequence();
    input.resetAll();

    for (Subsystem system : subsystems){
      system.autoInit();
    }
  } // initialize in auto

  @Override
  public void teleopInit(){
    // autoManager = AutoManager.getInstance();
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
  @Override
  public void autoContinuous(){
    autoManager.runSequence();
    for (Subsystem system : subsystems){
      system.run(RobotState.AUTO);
    }
  } // do continously in autonomous

  @Override
  public void teleopContinuous(){
    input.updateState();
    String[] testStrings = new String[] {"hi", "robot", "hello"};
    SmartDashboard.putStringArray("test", testStrings);
    
    if (state.getRobotState() == RobotState.SHOOTING){
      input.updateDrive();
      input.updateShooter();
      if (autoManager.sequenceEnded()){
        state.setRobotState(RobotState.TELEOP);
      }
    } // if doing auto mag shoot 
    else if (state.getRobotState() == RobotState.MAGLOAD) {
      input.updateDrive();
      input.updateShooter();
      if (autoManager.sequenceEnded()){
        state.setRobotState(RobotState.TELEOP);
      }
    } // if doing auto mag load 
    else {
      input.updateControllers();
    }

    for (Subsystem system : subsystems){
      system.run(state.getRobotState());
    }
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
      system.smartDashboard();
    }
  } // do continously always

  // ========== others ===========
  @Override 
  public void endCompetition(){
  } // endCompetition

} // Robot