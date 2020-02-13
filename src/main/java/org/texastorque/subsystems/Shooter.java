package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueTalon;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    private ArrayList<KPID> pidValues = new ArrayList<>();
    KPID kPIDLow = new KPID(0.08, 0, 8, 0.00902, -.5, .5); // tuned for 3000 rpm 
    KPID kPIDHigh = new KPID(0.2401, 0, 5, 0.00902, -.5, .5); // tuned for 6000 rpm 

    double flywheelSpeed = 0;
    
    // =========== motors ============
    private TorqueTalon flywheel = new TorqueTalon(Ports.FLYWHEEL_LEAD);

    // =========================================== methods ==============================================
    TalonSRX talonA = new TalonSRX(Ports.FLYWHEEL_FOLLOW);
    TalonSRX talonB = new TalonSRX(Ports.FLYWHEEL_LEAD);

    private void Shooter() {
        SmartDashboard.putNumber("ShooterInstantiated", 1);
        talonB.selectProfileSlot(0, 0);
        talonB.set(ControlMode.Velocity, 0);  
        talonA.set(ControlMode.Follower,Ports.FLYWHEEL_LEAD);
    } // constructor

    // ============= initialization ==========
    @Override 
    public void autoInit(){}

    @Override
    public void teleopInit(){}

    @Override 
    public void disabledInit(){}

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        SmartDashboard.putNumber("Shooter_Run", 1);
        if (state == RobotState.TELEOP){
            flywheelSpeed = input.getFlywheelSpeed();
        } // if in teleop 
        output();
    } // run at all times 

    @Override 
    public void output(){
        talonB.set(ControlMode.Velocity, 5000*Constants.RPM_VICTORSPX_CONVERSION);
        talonA.set(ControlMode.Follower, 1);
    } // output 

    // =========== continuous ==========
    @Override
    public void disabledContinuous() {}

    @Override
    public void autoContinuous() {}

    @Override
    public void teleopContinuous() {}

    // =========== others ===========
    @Override
    public void smartDashboard() {
        // SmartDashboard.putNumber("Flywheel RPM",flywheel.getVelocity()/Constants.RPM_VICTORSPX_CONVERSION);
    } // display all this to smart dashboard

    public static synchronized Shooter getInstance() {
        return instance == null ? (instance = new Shooter()) : instance;
    } // getInstance

} // Shooter 