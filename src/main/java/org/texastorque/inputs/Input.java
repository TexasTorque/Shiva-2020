package org.texastorque.inputs;

// ========= Imports ==========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.util.GenericController;
import org.texastorque.inputs.Feedback;

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
        } // update driver things

        if (operator != null){
        } // update operator things 


    } // update controllers

    public void resetAll(){
    } // reset all the things

    // ============= Drivebase ============

    private double DB_leftSpeed = 0;
    private double DB_rightSpeed = 0;

    public void updateDrive(){
        double leftRight = driver.getRightXAxis();
        DB_leftSpeed = -driver.getLeftYAxis() + 0.4 * Math.pow(leftRight, 2) * Math.signum(leftRight);
        DB_rightSpeed = -driver.getLeftYAxis() - 0.4 * Math.pow(leftRight, 2) * Math.signum(leftRight);
        if (driver.getYButtonPressed()){
            if (state.getRobotState() == RobotState.TELEOP) {
                state.setRobotState(RobotState.VISION);
            }
            else {
                state.setRobotState(RobotState.TELEOP);
            }
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

    // ============= Shooter ==============

    private volatile double flywheelSpeed = 0;

    public void updateShooter(){
        // for now this is controlling the rotary on Ray by position
        if (driver.getDPADDown()){
            flywheelSpeed = 2500;
        }
        if (driver.getDPADUp()){
            flywheelSpeed = 0;
        }
        if (driver.getDPADRight()){
          flywheelSpeed = 1000;
        }
    }

    public double getFlywheel(){
        return flywheelSpeed;
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
