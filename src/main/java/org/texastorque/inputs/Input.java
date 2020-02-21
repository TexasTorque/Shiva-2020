package org.texastorque.inputs;

import org.texastorque.auto.AutoManager;
import org.texastorque.constants.Constants;
// ========= Imports ==========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.util.GenericController;
import org.texastorque.util.TorqueTimer;

import edu.wpi.first.wpilibj.Timer;

public class Input {
    private static volatile Input instance;

    private volatile State state;
    private GenericController driver;
    private GenericController operator;
    // private TorqueTimer timer;

    private Input(){
        state = State.getInstance();
        driver = new GenericController(0, 0.1);
        operator = new GenericController(1, 0.1);
        // timer.start();
    } // Input constructor

    public void updateControllers() {

        if(driver != null){
            updateDrive();
            updateShooter();
            updateClimber();
            updateIntake();
            updateTest();
        } // update driver things

        if (operator != null){
            updateMagazine();
        } // update operator things 
    } // update controllers

    public void resetAll(){
    } // reset all the things

    // ============= Drivebase ============
    // driver controlled 
    private volatile double DB_leftSpeed = 0;
    private volatile double DB_rightSpeed = 0;

    public void updateDrive(){
        double leftRight = driver.getRightXAxis();
        if(hoodSetpoint == hoodSetpoints[0]){
            DB_leftSpeed = driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
            DB_rightSpeed = -driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
        }
        else{
            DB_leftSpeed = .2*(driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight));
            DB_rightSpeed = .2*(-driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight));
        }

        if (driver.getAButtonPressed()){
            state.setRobotState(RobotState.VISION);
        } else if (driver.getXButtonPressed()){
            state.setRobotState(RobotState.TELEOP);
        }

        if (driver.getLeftCenterButton()){
            Feedback.getInstance().resetDriveEncoders();
        }
    } // update the drivebase

    public void setState(RobotState stateSet){
        state.setRobotState(stateSet);
    }

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
    // driver controlled 
    private volatile double rotaryPosition_left;
    private volatile double rotaryPosition_right;
    private double rotarySpeed = 0;
    private int rollerSpeed = 0;
    // start position ---- neutral position ----- down position
    private double[] rotarySetpoints_left = {0, -15, -42};
    private double[] rotarySetpoints_right = {0, 15, 42};
    private int neutral = 1;

    public void updateIntake(){
        rollerSpeed = 0;
        if(driver.getBButtonPressed()){
            if(neutral == 1){
                neutral = 0;
            }
            else{
                neutral =1;
            }
        }
        rotaryPosition_left = rotarySetpoints_left[neutral];
        rotaryPosition_right = rotarySetpoints_right[neutral];
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
        if (driver.getDPADUp()){
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

    public void setRotaryPosition(int rotarySetIndex){
        rotaryPosition_left = rotarySetpoints_left[rotarySetIndex];
        rotaryPosition_right = rotarySetpoints_right[rotarySetIndex];
    } // ONLY TO BE USED IN AUTO 

    // public double getRotaryDiffLeft(){
    //     // DO THIS LATER 
    // }

    // ============ Magazine ============
    // operator controlled 
    double magVelocity_low = 0;
    double magVelocity_high = 0;
    double magVelocity_gate = 0;
    double magSpeed_low = .6; // keep this number positive
    double magSpeed_high = .5; // keep this number positive
    double magSpeed_gate = 1;


    public void updateMagazine(){
        magVelocity_low = 0;
        magVelocity_high = 0;
        magVelocity_gate = 0;

    
        if (operator.getLeftTrigger()){ // high mag - balls in 
            magVelocity_high = operator.getLeftZAxis() * magSpeed_high;
        }
        else if (operator.getLeftBumper()){ // high mag - balls out 
            magVelocity_high = - magSpeed_high;
        }
        if (operator.getRightTrigger()){ // low mag - balls in 
            magVelocity_low = - operator.getRightZAxis() * magSpeed_low;
        }
        else if (operator.getRightBumper()){ // low mag - balls out
            magVelocity_low = magSpeed_low;
        }

        // if(operator.getDPADUp()){   // shoot button - needs to start the sequence // TODO
        //     // magVelocity_gate = -magSpeed_gate;
        //     // magVelocity_high = magSpeed_high;
        //     // magVelocity_low = -magSpeed_low;     
        // }
        if(operator.getDPADDown()){ // gate on its own 
            magVelocity_gate = -magSpeed_gate;
        }
    } // update Magazine 

    public double getMagHigh(){
        return magVelocity_high;
    } // get Mag Direction

    public double getMagLow(){
        return magVelocity_low;
    } // get low mag direction

    public double getMagGate(){
        return magVelocity_gate;
    } // get gate mag direction

    public void setGate(boolean on){
        if (on){
            magVelocity_gate = -magSpeed_gate;
        } 
        else {
            magVelocity_gate = 0;
        }   
    } // setGate 

    public void setHighMag(boolean on){
        if (on){
            magVelocity_high = magSpeed_high;
        }
        else {
            magVelocity_high = 0;
        }
    } // set low mag 

    public void setLowMag(boolean on){
        if (on){
            magVelocity_low = -magSpeed_low;    
        }
        else {
            magVelocity_low = 0;
        }
    } // set Low mag 
    
    // ============= Climber ==============
    // driver controlled 
    private volatile double climberSpeed = .7;
    private volatile int climberStatus = 0;
    private volatile boolean climberServoLocked = true; 
    
    public void updateClimber(){
        if (driver.getDPADRight()){ // goes up
            // climberSpeed += .05;
            climberStatus = -1;
        }
        else if (driver.getDPADLeft()){ // goes down
            // climberSpeed -= -.05;
            climberStatus = 1;
        }
        else {
            climberStatus = 0;
        }
        if (driver.getLeftCenterButton()){
            climberServoLocked = false;
        }
        else if (driver.getRightCenterButton()){
            climberServoLocked = true;
        }
    } // update Climber 

    public int getClimberStatus(){
        return climberStatus;
    }

    public boolean getServoLocked(){
        return climberServoLocked;
    }

    // ============= Shooter ==============
    // operator controlled 
    private volatile boolean percentOutput = false;
    private volatile double flywheelPercent = 0; 
    private volatile double flywheelSpeed = 0;
    // min ---- mid ----- max 
    private volatile double[] hoodSetpoints = {0, 1, 15, 36};
    private volatile double hoodSetpoint;
    private volatile double hoodFine = 0;
    private volatile double shooterFine = 0;
    private volatile double flywheelEncoderSpeed = 0;

    public void updateShooter(){
        hoodSetpoint = hoodSetpoints[0];
        flywheelSpeed = 0;
        flywheelPercent = 0;

        hoodFine = -operator.getLeftYAxis() * 10;
        shooterFine = -operator.getRightYAxis() * 100;

        if (operator.getYButton()){ // layup shot 
            // flywheelSpeed = 1000*Constants.RPM_VICTORSPX_CONVERSION;
            flywheelSpeed = 4250 + shooterFine;
            if (!(hoodSetpoint > 26) && !(hoodSetpoint < 10)){
                hoodSetpoint = hoodSetpoints[1] + hoodFine;
            }
            else {
                hoodSetpoint = hoodSetpoints[1];
            }
        } 
        else if (operator.getBButton()){ // trench shot 
            flywheelSpeed = 5500 + shooterFine;
            flywheelPercent = 0.72;
            if (!(hoodSetpoint > 26) && !(hoodSetpoint < 10)){
                hoodSetpoint = hoodSetpoints[3] + hoodFine;
            }
            else {
                hoodSetpoint = hoodSetpoints[3];
            }
            
        }
        else if (operator.getAButton()){ // longshot™
            flywheelSpeed = 8000 + shooterFine;
            if (!(hoodSetpoint > 26) && !(hoodSetpoint < 10)){
                hoodSetpoint = hoodSetpoints[3] + hoodFine;
            }
            else {
                hoodSetpoint = hoodSetpoints[3];
            }
        }

        if (operator.getXButton()){ // limelight mode 
            // shoot everything = the whole sequence of events required in order to shoot 
        }
    } // update Shooter 

    public double getFlywheelPercent(){
        return flywheelPercent;
    }

    public double getFlywheelSpeed(){
        return flywheelSpeed;
    }

    public boolean getFlywheelPercentMode(){
        return percentOutput;
    }

    public void setFlywheelOutputType(boolean percentOutput){
        this.percentOutput = percentOutput;
    }

    public void setFlywheelPercent(double percent){
        flywheelPercent = percent;
    }

    public void setFlywheelSpeed(double speed){
        flywheelSpeed = speed;
    }

    public void setFlywheelEncoderSpeed(double speed){
        flywheelEncoderSpeed = speed;
    }

    public double getFlywheelEncoderSpeed(){
        return flywheelEncoderSpeed;
    }

    public double getHoodSetpoint(){
        return hoodSetpoint;
    }

    public void setHoodSetpoint(int index){
        hoodSetpoint = hoodSetpoints[index];
    }

    // =========== Testing ===========

    private double test1 = 0;
    private double test2 = 0;

    public void updateTest(){
        test1 = -driver.getLeftYAxis();
        test2 = -driver.getRightYAxis();
    }

    public double getTest1(){
        return test1;
    }

    public double getTest2(){
        return test2;
    }

    // =========== Others ============
    RobotState lastState = RobotState.AUTO;
    
    public void updateState(){
        if (operator.getDPADUp() && (lastState != RobotState.SHOOTING)){
            AutoManager.getInstance().runMagAutomatic(); 
            state.setRobotState(RobotState.SHOOTING);
            lastState = RobotState.SHOOTING;
        } 
        else if(operator.getDPADUp()){
            AutoManager.getInstance().runSequence();
        }
        else {
            if (lastState == RobotState.SHOOTING){
                AutoManager.getInstance().resetCurrentSequence();
            }
            if (lastState != RobotState.AUTO){
                state.setRobotState(RobotState.TELEOP);
                lastState = RobotState.TELEOP;
            }
        }

        if (operator.getDPADRight() && (lastState != RobotState.MAGLOAD)){
            AutoManager.getInstance().runMagLoad();
            state.setRobotState(RobotState.MAGLOAD);
            lastState = RobotState.MAGLOAD;
        }
        else if(operator.getDPADRight()){
            AutoManager.getInstance().runSequence();
        }
        else {
            if (lastState == RobotState.MAGLOAD){
                AutoManager.getInstance().resetCurrentSequence();
            }
            if (lastState != RobotState.AUTO){
                state.setRobotState(RobotState.TELEOP);
                lastState = RobotState.TELEOP;
            }
        }
    }

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