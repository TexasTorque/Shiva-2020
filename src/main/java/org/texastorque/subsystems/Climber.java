package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// ================== Climber ==================
public class Climber extends Subsystem{
    private static volatile Climber instance;

    // ============ variables =============

    // ============ motors ==============
    private CANSparkMax climber = new CANSparkMax(Ports.CLIMBER, MotorType.kBrushless);
    // private CANPIDController climberPID = climber.getPIDController();
    private CANEncoder climberEncoder = climber.getEncoder(EncoderType.kHallSensor, 4096);

    // =================== methods ==================
    private void Climber(){
        
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){

    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            SmartDashboard.putNumber("input", input.getClimberSpeed());
            // climber.set();
            // climberPID.setReference(-10000, ControlType.kVelocity, 0);
            SmartDashboard.putNumber("neo encoder", climberEncoder.getVelocity());
            SmartDashboard.putNumber("neo current", climber.getOutputCurrent());
        }
    } // run at all times 

    @Override 
    public void output(){

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

    public static Climber getInstance(){
        if (instance == null){
            synchronized(Climber.class){
                if (instance == null)
                    instance = new Climber();
            }
        }
        return instance;
    } // getInstance

} // Climber 
