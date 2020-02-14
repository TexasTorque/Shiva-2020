package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;

import edu.wpi.first.wpilibj.Servo;
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
    private double servoPos_left = 0;
    private double servoPos_right = 0;
    private double climberSpeed = 0;
    // ============ motors ==============
    private CANSparkMax climber_left = new CANSparkMax(Ports.CLIMBER_LEFT, MotorType.kBrushless);
    // private CANSparkMax climber2 = new CANSparkMax(Ports.CLIMBER2, MotorType.kBrushless);
    // === PID ===
    // private CANPIDController climberPID = climber.getPIDController();
    private CANEncoder climberEncoder1 = climber_left.getEncoder(EncoderType.kHallSensor, 4096);
    // private CANEncoder climberEncoder2 = climber2.getEncoder(EncoderType.kHallSensor, 4096);
    // private Servo climbServo_left = new Servo(Ports.CLIMB_SERVO_LEFT);

    // =================== methods ==================
    private void Climber(){
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
        // === PID ===
        // climberPID.setP(pidValues[0]);
        // climberPID.setI(pidValues[1]);
        // climberPID.setD(pidValues[2]);
        // climberPID.setOutputRange(pidValues[6], pidValues[7]);
    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            if (input.getServoLocked()){
                servoPos_left = 0;
            } else {
                servoPos_left = 0.5;
            }
            climberSpeed = input.getClimberStatus();
            // SmartDashboard.putNumber("input", input.getClimberSpeed());
            // ==== Raw Output ====
            // climber1.set(input.getMag());
            // climber2.set(0.5);
            // 5805 units = 770 rpm
            // ==== PID ====
            // climberPID.setReference(-300, ControlType.kVelocity);
            SmartDashboard.putNumber("neo encoder1", climberEncoder1.getVelocity());
            // SmartDashboard.putNumber("neo encoder2", climberEncoder2.getVelocity());
            SmartDashboard.putNumber("neo current1", climber_left.getOutputCurrent());
            // SmartDashboard.putNumber("neo current2", climber2.getOutputCurrent());
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        // climbServo_left.set(servoPos_left);
        SmartDashboard.putNumber("Climber speed", climberSpeed);
        climber_left.set(climberSpeed);
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