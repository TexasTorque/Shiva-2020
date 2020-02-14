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
    // driver controlled 
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
    // driver controlled 
    private volatile double rotaryPosition_left = 8.857;
    private volatile double rotaryPosition_right = -23.5;
    private double rotarySpeed = 0;
    private int rollerSpeed = 0;
    // start position ---- neutral position ----- down position
    private double[] rotarySetpoints_left = {0, -6, -42};
    private double[] rotarySetpoints_right = {0, 6, 42};

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
    // operator controlled 
    double magVelocity_low = 0;
    double magVelocity_high = 0;
    double magSpeed_low = 1; // keep this number positive
    double magSpeed_high = .9; // keep this number positive

    public void updateMagazine(){
        magVelocity_low = 0;
        magVelocity_high = 0;

        if (operator.getLeftTrigger()){ // high mag - balls in 
            magVelocity_high = operator.getRightZAxis() * magSpeed_high;
        }
        else if (operator.getLeftBumper()){ // high mag - balls out 
            magVelocity_low = magSpeed_low;
        }
        if (operator.getRightTrigger()){ // low mag - balls in 
            magVelocity_low = operator.getLeftZAxis() * magSpeed_low;
        }
        else if (operator.getRightBumper()){ // low mag - balls out
            magVelocity_low = magSpeed_low;
        }

        // if (operator.getRightTrigger()){ // bring balls up - low mag 
        //     // magVelocity_low = -magSpeed_low;
        //     magVelocity_low = operator.getLeftZAxis() * magSpeed_low;
        // }
        // else if(operator.getLeftTrigger()){ // bring balls down - low mag 
        //     magVelocity_low = magSpeed_low;
        // }
        // if (operator.getRightBumper()){ // brings balls up - high mag 
        //     magVelocity_high = magSpeed_high;
        // }
        // else if (operator.getLeftBumper()){ // brings balls down - high mag 
        //     magVelocity_high = -magSpeed_high;
        // }
    } // update Magazine 

    public double getMagHigh(){
        return magVelocity_high;
    } // get Mag Direction

    public double getMagLow(){
        return magVelocity_low;
    } // get low mag direction

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

    public double getTest(){
        return climberSpeed;
    }

    // ============= Shooter ==============
    // operator controlled 
    private volatile double flywheelSpeed = 0;
    private volatile double flywheelPercent = 0;
    // min ---- mid ----- max 
    private volatile double[] hoodSetpoints = {0, 15, 33};
    private volatile double hoodSetpoint = 22.0;
    private volatile double hoodFine = 0;
    private volatile double shooterFine = 0;
    private volatile double flywheelEncoderSpeed = 0;

    public void updateShooter(){
        //RPM is in testing state
        flywheelSpeed = 0;
        hoodFine = -operator.getLeftYAxis() * 10;
        shooterFine = -operator.getRightYAxis() * 100;
        hoodSetpoint = hoodSetpoints[0];

        if (operator.getYButton()){ // layup shot 
            // flywheelSpeed = 1000*Constants.RPM_VICTORSPX_CONVERSION;
            flywheelSpeed = 4000 + shooterFine;
            hoodSetpoint = hoodSetpoints[1] + hoodFine;
        } 
        else if (operator.getBButton()){ // trench shot 
            // flywheelSpeed = -1000*Constants.RPM_VICTORSPX_CONVERSION;
            flywheelSpeed = 6000 + shooterFine;
            hoodSetpoint = hoodSetpoints[2] + hoodFine;
        }
        else if (operator.getAButton()){ // longshot™
            flywheelSpeed = 10000 + shooterFine;
            hoodSetpoint = hoodSetpoints[0] + hoodFine;
        }
        if (operator.getXButton()){
            // shoot?? 
        }
    } // update Shooter 

    public double getFlywheelSpeed(){
        return flywheelSpeed;
    }

    public double getFlywheelPercent(){
        return flywheelPercent;
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