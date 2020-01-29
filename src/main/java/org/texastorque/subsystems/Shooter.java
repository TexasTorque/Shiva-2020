package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ========= Shooter ==========
public class Shooter extends Subsystem{
    private static volatile Shooter instance;

    // ======== variables ==========
    double flywheelSpeed = 6000*Constants.RPM_VICTORSPX_CONVERSION;
    // double flywheelSpeed = 4000*Constants.RPM_VICTORSPX_CONVERSION;
    double flywheelPercent = 1;
    // P, I, D, k 
    double[] pidHigh = new double[] {0.2401, 0, 5, 0.00902};
    double[] pidLow = new double[] {.08, 0, 8, 0.00902};
    boolean isHigh = true;
    private double flywheelVelocity = 0;

    // =========== motors ============
    TalonSRX talonLead = new TalonSRX(Ports.FLYWHEEL_LEAD);
    TalonSRX talonFollower = new TalonSRX(Ports.FLYWHEEL_FOLLOW);
    
    private void Shooter(){
        // PID STUFF 
        talonLead.selectProfileSlot(0,0);
        talonLead.set(ControlMode.Velocity, 0);
        talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
    } // constructor

    // ============= initialization ==========

    @Override 
    public void autoInit(){} // autoInit

    @Override
    public void teleopInit(){} // teleopInit

    @Override 
    public void disabledInit(){} // disabledInit

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){

            // // Bang Bang 
            // flywheelVelocity = talonLead.getSelectedSensorVelocity() / Constants.RPM_VICTORSPX_CONVERSION;
            // if (flywheelVelocity >= 6500){
            //     talonLead.set(ControlMode.PercentOutput, 0);
            //     talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
            // } 
            // else {
            //     talonLead.set(ControlMode.PercentOutput, .9);
            //     talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
            // }
            if (flywheelSpeed < 5000){
                isHigh = false;
            }
            // PID STUFF
            if (isHigh){
                talonLead.config_kP(0, pidHigh[0]);
                talonLead.config_kD(0, pidHigh[2]);
                talonLead.config_kF(0, pidHigh[3]);
            } else {
                talonLead.config_kP(0, pidLow[0]);
                talonLead.config_kD(0, pidLow[2]);
                talonLead.config_kF(0, pidLow[3]);
            }
            flywheelSpeed += input.getFlywheelSpeed();
            flywheelPercent += input.getFlywheelPercent();
            isHigh = true;
        } // if in teleop 
        output();
    } // run at all times 

    @Override 
    public void output(){
        // talonLead.set(ControlMode.Velocity, flywheelSpeed*Constants.RPM_VICTORSPX_CONVERSION);
        // SmartDashboard.putNumber("FlywheelPercent", flywheelPercent);
        // talonLead.set(ControlMode.PercentOutput, flywheelPercent);
        // talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
        // PID STUFF
        SmartDashboard.putNumber("FlywheelVelocity",flywheelSpeed);
        // SmartDashboard.putNumber("Flywheel Encoder ", )
        talonLead.set(ControlMode.Velocity, flywheelSpeed);
        talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
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
