package org.texastorque.subsystems;

// ============ imports ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
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
    KPID kPIDRotary = new KPID(0, 0, 0, 0, 0, 0);
    private double rotaryPosition = 0;

    // =========== motors ===========
    private TorqueMotor rotary = new TorqueMotor(ControllerType.SPARKMAX, Ports.INTAKE_ROTARY_LEAD);
    private TorqueMotor rollers = new TorqueMotor(ControllerType.SPARKMAX, Ports.INTAKE_ROLLERS);

    // =================== methods ==================
    private void Intake(){
        rotary.addFollower(Ports.INTAKE_ROTARY_FOLLOW);
        rotary.configurePID(kPIDRotary);
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){} // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.AUTO){

        }
        if (state == RobotState.TELEOP){
            rotaryPosition += input.getRotaryPosition();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        rotary.set(rotaryPosition, ControlType.kPosition);
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
