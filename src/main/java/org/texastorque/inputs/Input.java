package org.texastorque.inputs;

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
            updateShooter();
        } // update driver things

        if (operator != null){
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

    // ============= Shooter ==============

    private volatile double flywheelSpeed = 0;
    private volatile double flywheelPercent = 0;

    public void updateShooter(){
        // for now this is controlling the rotary on Ray by position
        if (driver.getYButtonReleased()){
            flywheelPercent = 0.05;
        } 
        else if (driver.getAButtonReleased()){
            flywheelPercent = -0.05;
        }
        else {
            flywheelPercent = 0;
        }
        if (driver.getBButtonReleased()){
            flywheelSpeed = 5000;
        } 
        else if (driver.getXButtonReleased()){
            flywheelSpeed = -5000;
        }
        else {
            flywheelSpeed = 0;
        }

    }

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
