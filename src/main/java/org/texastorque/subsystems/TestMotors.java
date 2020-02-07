package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// ================== TestMotors ==================
public class TestMotors extends Subsystem{
    private static volatile TestMotors instance;

    // ============ variables =============

    // ============ motors ==============
    private TorqueMotor testTalon = new TorqueMotor(ControllerType.VICTOR, Ports.TALON_PORT);
    private TorqueMotor testSparkMax = new TorqueMotor(ControllerType.SPARKMAX, Ports.SPARKMAX_PORT);

    // =================== methods ==================
    private void TestMotors(){
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

    public static TestMotors getInstance(){
        if (instance == null){
            synchronized(TestMotors.class){
                if (instance == null)
                    instance = new TestMotors();
            }
        }
        return instance;
    } // getInstance

} // TestMotors  
