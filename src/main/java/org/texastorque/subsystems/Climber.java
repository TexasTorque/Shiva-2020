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
    // pid Values = kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM
    private double[] pidValues = new double[] {0.01,0,0,0,0,0,-1,1}; 

    // ============ motors ==============
    private CANSparkMax climber = new CANSparkMax(Ports.CLIMBER, MotorType.kBrushless);
    private CANPIDController climberPID = climber.getPIDController();
    private CANEncoder climberEncoder = climber.getEncoder(EncoderType.kHallSensor, 4096);

    // =================== methods ==================
    private void Climber(){
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
        climberPID.setP(pidValues[0]);
        climberPID.setI(pidValues[1]);
        climberPID.setD(pidValues[2]);
        climberPID.setOutputRange(pidValues[6], pidValues[7]);
    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            SmartDashboard.putNumber("input", input.getClimberSpeed());
            // climber.set(input.getClimberSpeed());
            climberPID.setReference(-300, ControlType.kPosition);
            SmartDashboard.putNumber("neo encoder", climberEncoder.getPosition());
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
