package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private void DriveBase(){

    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        
    }

    @Override
    public void teleopInit(){

    }

    @Override 
    public void disabledInit(){

    }

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){

    }

    @Override 
    public void output(){

    }

    // =========== continuous ==========
    @Override
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override
    public void teleopContinuous(){}

    // =========== others ===========
    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static DriveBase getInstance(){
        if (instance == null){
            synchronized(DriveBase.class){
                if (instance == null)
                    instance = new DriveBase();
            }
        }
        return instance;
    }

} // Drivebase
