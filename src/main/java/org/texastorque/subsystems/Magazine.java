package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;
import org.texastorque.util.pid.KPIDGains;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ================== Climber ==================
public class Magazine extends Subsystem{
    private static volatile Magazine instance;

    // ============ variables =============
    private KPID beltPID = new KPID(0, 0, 0, 0, -1, 1);
    private double beltSpeed = 0;

    // ============ motors ==============
    private TorqueMotor belt = new TorqueMotor(ControllerType.TALONSRX, Ports.BELT_LEAD);

    // =================== methods ==================
    private void Climber(){
        belt.addFollower(Ports.BELT_FOLLOW);
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){} // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.AUTO){
        }
        if (state == RobotState.TELEOP){
            beltSpeed = input.getMag();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        belt.set(beltSpeed); // running raw output rn (maybe add pid later?)
    } // output

    // ============= continuous =============
    @Override 
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override 
    public void teleopContinuous(){}

    // ============ others ==========

    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static Magazine getInstance(){
        if (instance == null){
            synchronized(Magazine.class){
                if (instance == null)
                    instance = new Magazine();
            }
        }
        return instance;
    } // getInstance

} // Climber 
