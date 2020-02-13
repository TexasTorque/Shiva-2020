package org.texastorque.inputs;

import org.texastorque.constants.Constants;
// ========= Imports ==========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.util.GenericController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Input {
    private static volatile Input instance;

    private volatile State state;
    private GenericController driver;
    private GenericController operator;

    private Input(){
        state = State.getInstance();
        driver = new GenericController(0, 0.1);
        operator = new GenericController(1, 0.1);
    } // Input constructor

    public void updateControllers() {

        if(driver != null){
            updateDrive();
            updateShooter();
            updateClimber();
            updateIntake();
        } // update driver things

        if (operator != null){
            updateMagazine();
        } // update operator things 
    } // update controllers

    public void resetAll(){
    } // reset all the things

    // ============= Drivebase ============

    private volatile double DB_leftSpeed = 0;
    private volatile double DB_rightSpeed = 0;

    public void updateDrive(){
        double leftRight = driver.getRightXAxis();
        DB_leftSpeed = driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
        DB_rightSpeed = -driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
        if (driver.getAButtonPressed()){
            state.setRobotState(RobotState.VISION);
        } else if (driver.getXButtonPressed()){
            state.setRobotState(RobotState.TELEOP);
        }
    } // update the drivebase

    public void resetDrive(){
        DB_leftSpeed = 0;
        DB_rightSpeed = 0;
    } // set speeds to 0

    public double getDBLeft(){
        return DB_leftSpeed;
    } // return left speed

    public double getDBRight(){
        return DB_rightSpeed;
    } // return right speed

    public void setDBLeftSpeed(double leftSpeed){
        DB_leftSpeed = leftSpeed;
    } // TO BE USED IN AUTO ONLY

    public void setDBRightSpeed(double rightSpeed){
        DB_rightSpeed = rightSpeed;
    } // TO BE USED IN AUTO ONLY

    // ============= Intake ==============

    private volatile double rotaryPosition_left = 8.857;
    private volatile double rotaryPosition_right = -23.5;
    private double rotarySpeed = 0;
    private int rollerSpeed = 0;
    // start position ---- up position ----- down position 
    private double[] rotarySetpoints_left = {17.095, 0.524, -24.547};
    private double[] rotarySetpoints_right = {-26.262, -11.191, 17.881};

    public void updateIntake(){
        rollerSpeed = 0;
        rotaryPosition_left = rotarySetpoints_left[1];
        rotaryPosition_right = rotarySetpoints_right[1];
        if (driver.getRightTrigger()){
            rotaryPosition_left = rotarySetpoints_left[2];
            rotaryPosition_right = rotarySetpoints_right[2];
            rollerSpeed = 1;
        }
        else if (driver.getLeftTrigger()){
            rotaryPosition_left = rotarySetpoints_left[2];
            rotaryPosition_right = rotarySetpoints_right[2];
            rollerSpeed = -1;
        }
        else if (driver.getDPADUp()){
            rotaryPosition_left = rotarySetpoints_left[0];
            rotaryPosition_right = rotarySetpoints_right[0];
        }
    } // update Intake 

    public double getRotarySpeed(){
        return rotarySpeed;
    }

    public double getRotaryPositionLeft(){
        return rotaryPosition_left;
    }

    public double getRotaryPositionRight(){
        return rotaryPosition_right;
    }

    public int getRollerSpeed(){
        return rollerSpeed;
    }

    // ============ Magazine ============
    double magVelocity_low = 0;
    double magVelocity_high = 0;
    double magSpeed_low = 1; // keep this number positive
    double magSpeed_high = .9; // keep this number positive

    public void updateMagazine(){
        magVelocity_low = 0;
        magVelocity_high = 0;
        if (operator.getDPADUp()){
            magVelocity_low = -magSpeed_low;
        }
        else if(operator.getDPADDown()){
            magVelocity_low = magSpeed_low;
        }
        if (operator.getYButton()){ // brings balls up
            magVelocity_high = magSpeed_high;
        }
        else if (operator.getAButton()){ // brings balls down 
            magVelocity_high = -magSpeed_high;
        }
    } // update Magazine 

    public double getMagHigh(){
        return magVelocity_high;
    } // get Mag Direction

    public double getMagLow(){
        return magVelocity_low;
    } // get low mag direction

    // ============= Climber ==============
    private volatile double climberSpeed = .7;
    private volatile int climberStatus = 0;
    private volatile boolean climberServoLocked = true; 
    
    public void updateClimber(){
        if (operator.getDPADRight()){ // goes up
            // climberSpeed += .05;
            climberStatus = -1;
        }
        else if (operator.getDPADLeft()){ // goes down
            // climberSpeed -= -.05;
            climberStatus = 1;
        }
        else {
            climberStatus = 0;
        }
        if (operator.getLeftCenterButton()){
            climberServoLocked = false;
        }
        else if (operator.getRightCenterButton()){
            climberServoLocked = true;
        }
    } // update Climber 

    public int getClimberStatus(){
        return climberStatus;
    }

    public boolean getServoLocked(){
        return climberServoLocked;
    }

    public double getTest(){
        return climberSpeed;
    }

    // ============= Shooter ==============

    private volatile double flywheelSpeed = 0;
    private volatile double flywheelPercent = 0;
    private volatile double[] hoodSetpoints = {-30, -18, -4.0};
    private volatile double hoodSetpoint = -15;

    public void updateShooter(){
        if (driver.getBButton()){
            // flywheelSpeed = 1000*Constants.RPM_VICTORSPX_CONVERSION;
            flywheelPercent = .3;
            flywheelSpeed = 4000;
        } 
        else if (driver.getXButtonReleased()){
            // flywheelSpeed = -1000*Constants.RPM_VICTORSPX_CONVERSION;
        }
        else {
            // flywheelSpeed = 0;
            flywheelPercent = 0;
            flywheelSpeed = 0;
        }
        if(operator.getBButton()){
            hoodSetpoint = hoodSetpoints[2];
        }
        else if (operator.getXButton()){
            hoodSetpoint = hoodSetpoints[0];
        }
        else{
            hoodSetpoint = hoodSetpoints[1];
        }

    } // update Shooter 

    public double getFlywheelSpeed(){
        return flywheelSpeed;
    }

    public double getFlywheelPercent(){
        return flywheelPercent;
    }
    private double flywheelEncoderSpeed = 0;

    public void setFlywheelEncoderSpeed(double speed){
        flywheelEncoderSpeed = speed;
    }

    public double getFlywheelEncoderSpeed(){
        return flywheelEncoderSpeed;
    }

    public double getHoodSetpoint(){
        return hoodSetpoint;
    }

    // =========== Others ============
    
    public RobotState getState(){
        return state.getRobotState();
    }

    // =========== Input =============

    public static Input getInstance() {
        if (instance == null) {
            synchronized (Input.class){
                if (instance == null)
                    instance = new Input();
            }
        }
        return instance;
    } // getInstance 
} // Input