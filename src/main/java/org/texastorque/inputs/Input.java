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
        DB_leftSpeed = -driver.getLeftYAxis() + 0.4 * Math.pow(leftRight, 2) * Math.signum(leftRight);
        DB_rightSpeed = -driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 2) * Math.signum(leftRight);
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

    private volatile double rotaryPosition = 0;
    private double rotarySpeed = 0;
    private int rollerStatus = 0;

    public void updateIntake(){
        // if (driver.getYButtonReleased()){
        //     rotaryPosition += 100;
        // }
        // else if (driver.getAButtonReleased()){
        //     rotaryPosition -= 100;
        // }
        // if (operator.getRightBumper()){
        //     rotarySpeed = 0.3;
        // }
        // else if (operator.getLeftBumper()){
        //     rotarySpeed = - 0.3;
        // }
        rotaryPosition = -driver.getRightYAxis();
        if (driver.getDPADUp()){
            rollerStatus = 1;
        }
        else if(driver.getDPADRight()){
            rollerStatus = 0;
        }
        else if(driver.getDPADDown()){
            rollerStatus = -1;
        }
    } // update Intake 

    public double getRotarySpeed(){
        return rotarySpeed;
    }

    public double getRotaryPosition(){
        return rotaryPosition;
    }

    public int getRollerStatus(){
        return rollerStatus;
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
    private volatile double climberSpeed = 0;
    
    public void updateClimber(){
    } // update Climber 

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
