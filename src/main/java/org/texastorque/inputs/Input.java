package org.texastorque.inputs;

import org.texastorque.constants.Constants;
// ========= Imports ==========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.Feedback;
import org.texastorque.torquelib.component.TorqueSparkMax;
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
            // updateDrive();
            // updateShooter();
            // updateClimber();
            // updateIntake();
        } // update driver things

        if (operator != null){
            updateColorMunch();
            // updateMagazine();
        } // update operator things 
    } // update controllers

    public void resetAll(){
    } // reset all the things

    // ========== ColorMunch ============
    private volatile double rotaryPosition_Color = 8.857; // you will want to change this....
    private double rotarySpeedColor = 0;
    private int colorSwitcher = 0;
    public static boolean colorIsUp = false;
    // startpos... next pos (switching between 2)
    private double[] rotarySetpoints_Color = {0, -45};

    public void updateColorMunch(){  //Check where used "update intake"
    //sets init position of setpoint to start
    rotaryPosition_Color = rotarySetpoints_Color[0];

    //If colorSwitcher = 0, set 1 (vice Vers)
        if (driver.getDPADDown()){
            if(colorSwitcher == 0){
                colorSwitcher = 1;
                colorIsUp = true;
            }else if(colorSwitcher == 1){
                colorSwitcher = 0;
                colorIsUp = false;
            }
        rotaryPosition_Color = rotarySetpoints_Color[colorSwitcher];
        }
    }

    public double getRotaryPositionColor(){
        return rotaryPosition_Color;
    }

    public double getRotarySpeedColor(){
        return rotarySpeedColor;
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


    //============ FOR NOW 0 UNTIL GET BOTH WORKING TOGETHER ==========
	public double getMagGate() {
		return 0;
    }
    
} // Input
