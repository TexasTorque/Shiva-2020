package org.texastorque.subsystems;

// ============ imports ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.ControlType;

import java.util.ArrayList;

// =========== Intake =============
public class Intake extends Subsystem{
    private static volatile Intake instance;

    // =========== variables ===========
    // pid Values = kP, kI, kD, kFF, kMinOutput, kMaxOutput
    KPID kPIDRotary_left = new KPID(0.07, 0.00005, 0.00002, 0, -.8, .8);
    KPID kPIDRotary_right = new KPID(0.07, 0.00005, 0.00002, 0, 1, 1);
    private double rotaryPosition_left = 0;
    private double rotaryPosition_right = 0;
    private double rollerSpeed = 0;

    // =========== motors ===========
    private TorqueSparkMax rotary_left = new TorqueSparkMax(Ports.INTAKE_ROTARY_LEFT);
    private TorqueSparkMax rotary_right = new TorqueSparkMax(Ports.INTAKE_ROTARY_RIGHT);
    private TorqueSparkMax rollers = new TorqueSparkMax(Ports.INTAKE_ROLLERS);

    // =================== methods ==================
    private Intake(){
        rotary_left.configurePID(kPIDRotary_left);
        rotary_right.configurePID(kPIDRotary_right);
        rotary_left.tareEncoder();
        rotary_right.tareEncoder();
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
        rotary_left.tareEncoder();
        rotary_right.tareEncoder();
    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.AUTO){
        } // auto 
        if (state == RobotState.TELEOP){
            rollerSpeed = input.getRollerSpeed();
            rotaryPosition_left = input.getRotaryPositionLeft();
            rotaryPosition_right = input.getRotaryPositionRight();
            
            SmartDashboard.putNumber("rotary_left.position", rotary_left.getPosition());
            SmartDashboard.putNumber("rotary_right.position", rotary_right.getPosition());
            SmartDashboard.putNumber("rotaryPosition_left", rotaryPosition_left);
            SmartDashboard.putNumber("rotaryPosition_right", rotaryPosition_right);
        } // teleop
        output();
    } // run at all times 

    @Override 
    public void output(){
        rollers.set(rollerSpeed);
        SmartDashboard.putNumber("output_left_current", rotary_left.getCurrent());
        SmartDashboard.putNumber("output_right_current", rotary_right.getCurrent());
        // rotary_left.set(rotaryPosition_left, ControlType.kPosition);
        // rotary_right.set(rotaryPosition_right, ControlType.kPosition);
    } // output

    // ============= continuous =============
    @Override 
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override 
    public void teleopContinuous(){}

    // ========= get methods ==========

    public double getRotaryPosLeft(){
        return rotary_left.getPosition();
    }

    public double getRotaryPosRight(){
        return rotary_right.getPosition();
    }

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
