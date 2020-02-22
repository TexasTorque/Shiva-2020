package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.component.TorqueSparkMax;
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
    // private double servoPos_left = 0;
    // private double servoPos_right = 0;
    private double climberSpeed_left = 0;
    private double climberSpeed_right = 0;
    // ============ motors ==============
    private TorqueSparkMax climber_left = new TorqueSparkMax(Ports.BELT_GATE);
    private TorqueSparkMax climber_right = new TorqueSparkMax(Ports.INTAKE_ROLLERS);
    // private Servo climbServo_left = new Servo(Ports.CLIMB_SERVO_LEFT);

    // =================== methods ==================
    private Climber(){
        // climber_left = new TorqueSparkMax(Ports.CLIMBER_LEFT);
        // climber_right = new TorqueSparkMax(Ports.CLIMBER_RIGHT);
    } // constructor 

    @Override
    public void autoInit(){
    }

    @Override
    public void teleopInit(){
        
    } // teleop init

    @Override 
    public void disabledInit(){
    }

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            // if (input.getServoLocked()){
            //     servoPos_left = 0;
            //     servoPos_right = 0;
            // } else {
            //     servoPos_left = 0.5;
            //     servoPos_right = 0;
            // }
            // climberSpeed = input.getClimberStatus();
            climberSpeed_left = input.getClimberLeft();
            climberSpeed_right = input.getClimberRight();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        // climbServo_left.set(servoPos_left);
        // climber_left.set(climberSpeed);
        SmartDashboard.putNumber("climb left", climberSpeed_left);
        SmartDashboard.putNumber("climb right", climberSpeed_right);
        climber_left.set(1);
        climber_right.set(1);
    } // output

    // ============= continuous =============
    @Override 
    public void disabledContinuous(){
    }

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