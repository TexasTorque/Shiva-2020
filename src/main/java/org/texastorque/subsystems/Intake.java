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
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ctre.phoenix.motorcontrol.ControlMode;

// =========== Intake =============
public class Intake extends Subsystem{
    private static volatile Intake instance;

    // =========== variables ===========
        // pid Values = kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM
        private double[] pidValues = new double[] {0.001,0,0,0,0,0,-.5,.5}; 

    // =========== motors ===========
    private CANSparkMax intake1 = new CANSparkMax(Ports.INTAKE1, MotorType.kBrushless);
    private CANSparkMax intake2 = new CANSparkMax(Ports.INTAKE2, MotorType.kBrushless);
    private CANSparkMax intake3 = new CANSparkMax(Ports.INTAKE3, MotorType.kBrushless);
    
    private CANEncoder intakeEncoder1 = intake1.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder intakeEncoder2 = intake2.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder intakeEncoder3 = intake3.getEncoder(EncoderType.kHallSensor, 4096);

    // ============ others =============
    // === PID ===
    // private CANPIDController intakePID = intake.getPIDController();
    
    // =================== methods ==================
    private void Intake(){
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
        // === PID ===
        // intakePID.setP(pidValues[0]);
        // intakePID.setI(pidValues[1]);
        // intakePID.setD(pidValues[2]);
        // intakePID.setOutputRange(pidValues[6], pidValues[7]);
    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
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

    public static Intake getInstance(){
        if (instance == null){
            synchronized(Intake.class){
                if (instance == null)
                    instance = new Intake();
            }
        }
        return instance;
    } // getInstance
} // Intake
