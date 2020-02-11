package org.texastorque;

// ========= imports ======== 
import org.texastorque.subsystems.*;
import org.texastorque.auto.AutoManager;
import org.texastorque.inputs.*;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.base.TorqueIterative;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

// =============================== Robot ===============================

public class Robot extends TorqueIterative {

  // make instances of subsystems to later place into arraylist 
  private ArrayList<Subsystem> subsystems;
  private Subsystem driveBase = DriveBase.getInstance();
  // private Subsystem shooter = Shooter.getInstance();
  // private Subsystem climber = Climber.getInstance();
  private Subsystem intake = Intake.getInstance();
  // private Subsystem magazine = Magazine.getInstance();
  // private Subsystem testMotors = TestMotors.getInstance();
  
  // make instances of other useful classes 
  private State state = State.getInstance();
  private Input input = Input.getInstance();
  private Feedback feedback = Feedback.getInstance();
  private AutoManager autoManager = AutoManager.getInstance();

  // ======= initialize ============
  public void robotInit() {
    initSubsystems();    
  } // initialize robot

  public void initSubsystems(){
    subsystems = new ArrayList<Subsystem>();
    subsystems.add(driveBase);
    // subsystems.add(shooter);
    // subsystems.add(climber);
    subsystems.add(intake);
    // subsystems.add(magazine);
    // subsystems.add(testMotors);
  } // initialize subsystems 

  @Override
  public void autoInit(){
    state.setRobotState(RobotState.AUTO);
    autoManager.chooseSequence();
    input.resetAll();

    for(Subsystem system : subsystems){
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
      system.run(RobotState.TELEOP);
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
      system.disabledContinuous();
    }
  } // do continously always

  // ========== others ===========
  @Override 
  public void endCompetition(){
  } // endCompetition

} // Robot