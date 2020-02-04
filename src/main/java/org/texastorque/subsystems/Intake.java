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

import java.util.ArrayList;

// =========== Intake =============
public class Intake extends Subsystem{
    private static volatile Intake instance;

    // =========== variables ===========
    // pid Values = kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM
    

    // =========== motors ===========
    private CANSparkMax rotaryLeft = new CANSparkMax(Ports.INTAKE_ROTARY_LEFT, MotorType.kBrushless);
    private CANSparkMax rotaryRight = new CANSparkMax(Ports.INTAKE_ROTARY_RIGHT, MotorType.kBrushless);
    private CANSparkMax intakeWheels = new CANSparkMax(Ports.INTAKE_WHEELS, MotorType.kBrushless);
    
    private CANEncoder rotaryLeftEncoder = rotaryLeft.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder rotaryRightEncoder = rotaryRight.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder intakeWheelsEncoder = intakeWheels.getEncoder(EncoderType.kHallSensor, 4096);

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
        // rotaryLeft.set()
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
