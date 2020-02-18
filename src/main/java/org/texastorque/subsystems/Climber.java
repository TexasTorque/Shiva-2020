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
    // private CANSparkMax climber_left = new CANSparkMax(Ports.CLIMBER_LEFT, MotorType.kBrushless);
    // private CANSparkMax climber2 = new CANSparkMax(Ports.CLIMBER2, MotorType.kBrushless);
    private Servo climbServo_left = new Servo(Ports.CLIMB_SERVO_LEFT);

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
            if (input.getServoLocked()){
                servoPos_left = 0;
                servoPos_right = 0;
            } else {
                servoPos_left = 0.5;
                servoPos_right = 0;
            }
            climberSpeed = input.getClimberStatus();

        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        climbServo_left.set(servoPos_left);
        // climber_left.set(climberSpeed);
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
        SmartDashboard.putNumber("Climber speed", climberSpeed);
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