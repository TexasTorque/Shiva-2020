package org.texastorque.inputs;

import org.texastorque.constants.Constants;
// ========= Imports ==========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.util.GenericController;

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
        DB_leftSpeed = -driver.getLeftYAxis() + 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
        DB_rightSpeed = -driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 4) * Math.signum(leftRight);
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
    private double[] rotarySetpoints_left = {24.857, 8.857, -13.571};
    private double[] rotarySetpoints_right = {-38.785, -23.500, 1.167};

    public void updateIntake(){
        rollerSpeed = 0;
        if (driver.getDPADDown()){
            rotaryPosition_left = rotarySetpoints_left[2];
            rotaryPosition_right = rotarySetpoints_right[2];
        }
        else if (driver.getDPADRight()){
            rotaryPosition_left = rotarySetpoints_left[1];
            rotaryPosition_right = rotarySetpoints_right[1];
        }
        else if (driver.getDPADUp()){
            rotaryPosition_left = rotarySetpoints_left[0];
            rotaryPosition_right = rotarySetpoints_right[0];
        }
        if (driver.getRightTrigger()){
            rollerSpeed= 1;
        }
        else if(driver.getLeftTrigger()){
            rollerSpeed = -1;
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
    double magVelocity = 0;
    double magSpeed = 1; // keep this number positive

    public void updateMagazine(){
        magVelocity = 0;
        if (operator.getLeftBumper()){
            magVelocity = -magSpeed;
        }
        else if(operator.getRightBumper()){
            magVelocity = magSpeed;
        }
    } // update Magazine 

    public double getMag(){
        return magVelocity;
    } // get Mag Direction

    // ============= Climber ==============
    private volatile double climberSpeed = .7;
    
    public void updateClimber(){
        if (driver.getYButtonPressed()){
            climberSpeed += .05;
        }
        else if (driver.getAButtonPressed()){
            climberSpeed -= -.05;
        }
    } // update Climber 

    public double getTest(){
        return climberSpeed;
    }

    // ============= Shooter ==============

    private volatile double flywheelSpeed = 0;
    private volatile double flywheelPercent = 0;

    public void updateShooter(){
        if (driver.getBButtonReleased()){
            flywheelSpeed = 1000*Constants.RPM_VICTORSPX_CONVERSION;
        } 
        else if (driver.getXButtonReleased()){
            flywheelSpeed = -1000*Constants.RPM_VICTORSPX_CONVERSION;
        }
        else {
            flywheelSpeed = 0;
        }
    } // update Shooter 

    public double getFlywheelSpeed(){
        return flywheelSpeed;
    }

    public double getFlywheelPercent(){
        return flywheelPercent;
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
