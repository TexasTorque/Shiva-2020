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
    private TorqueSparkMax beltHigh = new TorqueSparkMax(Ports.BELT_HIGH);
    private TorqueSparkMax beltLow = new TorqueSparkMax(Ports.BELT_LOW);
    private TorqueSparkMax beltGate = new TorqueSparkMax(Ports.BELT_GATE);

    // =================== methods ==================
    public Magazine(){
        beltHigh.setAlternateEncoder();
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){} // teleop init

    @Override 
    public void disabledInit(){}

    //updating feedback
    public void update(){
        feedback.setShooterVelocity(beltHigh.getAlternateVelocity());
    }
    
    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        update();
        if (state == RobotState.AUTO){
            beltSpeed_high = input.getMagHigh();
            beltSpeed_low = input.getMagLow();
            beltSpeed_gate = input.getMagGate();
        }
        if (state == RobotState.TELEOP || state == RobotState.VISION){
            beltSpeed_high = input.getMagHigh();
            beltSpeed_low = input.getMagLow();
            beltSpeed_gate = input.getMagGate();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        // input.setFlywheelEncoderSpeed(beltHigh.getAlternateVelocity());
        SmartDashboard.putNumber("HighMagSpeed", beltSpeed_high); // test z axis 
        SmartDashboard.putNumber("LowMagSpeed", beltSpeed_low); // test z axis 
        SmartDashboard.putNumber("GateMagSpeed", beltSpeed_gate);
        beltHigh.set(beltSpeed_high); // running raw output rn (maybe add pid later?)
        beltLow.set(beltSpeed_low);
        beltGate.set(beltSpeed_gate);
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