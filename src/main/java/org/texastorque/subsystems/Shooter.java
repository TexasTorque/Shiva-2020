package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    double flywheelSpeed = 0;
    TalonSRX talonA;
    TalonSRX talonB;

    private void Shooter() {
        SmartDashboard.putNumber("ShooterInstantiated", 1);
        talonA = new TalonSRX(Ports.FLYWHEEL_A);
        talonB = new TalonSRX(Ports.FLYWHEEL_B);
        talonB.selectProfileSlot(0, 0);
        talonB.set(ControlMode.Velocity, 0);  
        talonA.set(ControlMode.Follower,Ports.FLYWHEEL_B);
    } // constructor

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        
    }

    @Override
    public void teleopInit(){

    }

    @Override 
    public void disabledInit(){

    }

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        SmartDashboard.putNumber("Shooter_Run", 1);
        if (state == RobotState.TELEOP){
            flywheelSpeed = input.getFlywheel();
        } // if in teleop 
        output();
    } // run at all times 

    @Override 
    public void output(){
        talonB.set(ControlMode.Velocity, 7000*Constants.conversion_Shooter);
        talonA.set(ControlMode.Follower, 1);
    } // output 

    // =========== continuous ==========
    @Override
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override
    public void teleopContinuous(){}

    // =========== others ===========
    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static Shooter getInstance(){
        if (instance == null){
            synchronized(Shooter.class){
                if (instance == null)
                    instance = new Shooter();
            }
        }
        return instance;
    } // getInstance

} // Shooter 
