package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ========= Shooter ==========
public class Shooter extends Subsystem{
    private static volatile Shooter instance;

    double flywheelSpeed = 5000;
    TalonSRX talonLead;
    TalonSRX talonFollower;

    private void Shooter(){
        talonLead = new TalonSRX(Ports.FLYWHEEL_LEAD);
        talonFollower = new TalonSRX(Ports.FLYWHEEL_FOLLOW);
        talonLead.selectProfileSlot(0, 0);
        talonLead.set(ControlMode.Velocity, 0);
        talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
        SmartDashboard.putBoolean("ReachesShooterConstructor", true);
    } // constructor

    // ============= initialization ==========
    @Override 
    public void autoInit(){}

    @Override
    public void teleopInit(){
        flywheelSpeed=5000;
    } // teleopInit

    @Override 
    public void disabledInit(){}

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        flywheelSpeed += input.getFlywheel();
        // if (state == RobotState.TELEOP){
        //     flywheelSpeed += input.getFlywheel();
        // } // if in teleop 
        output();
    } // run at all times 

    @Override 
    public void output(){
        // talonLead.set(ControlMode.Velocity, flywheelSpeed*Constants.RPM_VICTORSPX_CONVERSION);
        talonLead.set(ControlMode.Velocity, flywheelSpeed);
        talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
        SmartDashboard.putBoolean("ReachesOutput", true);
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
