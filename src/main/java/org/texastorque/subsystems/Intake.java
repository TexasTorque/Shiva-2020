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
    KPID kPIDRotary = new KPID(0, 0, 0, 0, 0, 0);
    private double rotaryPosition = 0;
    private int rollerStatus = 0;
    private double rollerSpeed = 0;

    // =========== motors ===========
    private TorqueSparkMax rotary = new TorqueSparkMax(Ports.INTAKE_ROTARY_LEAD);
    private TorqueSparkMax rollers = new TorqueSparkMax(Ports.INTAKE_ROLLERS);

    // =================== methods ==================
    private void Intake(){
        rotary.addFollower(Ports.INTAKE_ROTARY_FOLLOW);
        // add once working 
        // rotary.configurePID(kPIDRotary);
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
        } // auto 
        if (state == RobotState.TELEOP){
            // rotaryPosition += input.getRotaryPosition(); // right now this is set so rotary position is actually a v small output
            rotaryPosition = input.getRotaryPosition();
            rollerStatus = input.getRollerStatus();
            switch(rollerStatus){
                case -1:
                    rollerSpeed = -0.3;
                    break;
                case 0: 
                    rollerSpeed = 0;
                    break;
                case 1: 
                    rollerSpeed = 0.3;
                    break;
            } // direction of roller spin if spinning 
        } // teleop
        output();
    } // run at all times 

    @Override 
    public void output(){
        rotary.set(rotaryPosition);
        rollers.set(rollerSpeed);
        // add once working 
        // rotary.set(rotaryPosition, ControlType.kPosition);
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
