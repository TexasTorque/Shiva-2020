package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;

import com.revrobotics.SparkMax;

import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;
import org.texastorque.util.pid.KPIDGains;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ================== Climber ==================
public class Magazine extends Subsystem{
    private static volatile Magazine instance;

    // ============ variables =============
    private KPID beltPID = new KPID(0, 0, 0, 0, -1, 1);
    private double beltSpeed_high = 0;
    private double beltSpeed_low = 0;
    private double beltSpeed_gate = 0;

    // ============ motors ==============
    private TorqueSparkMax beltGate = new TorqueSparkMax(Ports.BELT_GATE);

    // =================== methods ==================
    public Magazine(){
        
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){} // teleop init

    @Override 
    public void disabledInit(){}

    //updating feedback
    public void update(){
        
    }
    
    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        update();
        if (state == RobotState.AUTO){
        }
        if (state == RobotState.TELEOP || state == RobotState.VISION){
            beltSpeed_gate = input.getMagGate();
        }
        output();
    } // run at all times 

    // ==== testing?? ======

    @Override 
    public void output(){
        SmartDashboard.putNumber("GateMagSpeed", beltSpeed_gate);
        if(feedback.shouldGateRun()){
            beltGate.set(0.6);

        }else if(feedback.shouldRunToColor()){
            beltGate.set(0.6);
        }else{
            beltGate.set(beltSpeed_gate);
        }
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

} 