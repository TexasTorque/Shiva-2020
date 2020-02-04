package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.util.pid.IConfiguratorPlugin;
import org.texastorque.util.pid.IGainProvider;
import org.texastorque.util.pid.KPIDGains;
import org.texastorque.util.pid.PIDConfigurator;
import org.texastorque.constants.*;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.*;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    private ArrayList<Object> pidValues = new ArrayList<Object>();

    double flywheelSpeed = 6000 * Constants.RPM_VICTORSPX_CONVERSION;
    double flywheelPercent = 1;

    // =========== motors ============
    TalonSRX talonLead = new TalonSRX(Ports.FLYWHEEL_LEAD);
    TalonSRX talonFollower = new TalonSRX(Ports.FLYWHEEL_FOLLOW);


    private Shooter() {
    } // constructor

    // ============= initialization ==========

    @Override
    public void autoInit() {
    } // autoInit

    @Override
    public void teleopInit() {
    } // teleopInit

    @Override
    public void disabledInit() {
    } // disabledInit

    // ============ actually doing stuff ==========
    @Override
    public void run(RobotState state) {
        if (state == RobotState.TELEOP) {

            // // Bang Bang
            // flywheelVelocity = talonLead.getSelectedSensorVelocity() /
            // Constants.RPM_VICTORSPX_CONVERSION;
            // if (flywheelVelocity >= 6500){
            // talonLead.set(ControlMode.PercentOutput, 0);
            // talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
            // }
            // else {
            // talonLead.set(ControlMode.PercentOutput, .9);
            // talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
            // }

            flywheelSpeed += input.getFlywheelSpeed();
            flywheelPercent += input.getFlywheelPercent();
        } // if in teleop
        output();
    } // run at all times

    @Override
    public void output() {
        // talonLead.set(ControlMode.Velocity,
        // flywheelSpeed*Constants.RPM_VICTORSPX_CONVERSION);
        // SmartDashboard.putNumber("FlywheelPercent", flywheelPercent);
        // talonLead.set(ControlMode.PercentOutput, flywheelPercent);
        // talonFollower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
        // PID STUFF
        double measuredSpeed = talonLead.getSelectedSensorVelocity() / Constants.RPM_VICTORSPX_CONVERSION;
        SmartDashboard.putNumber("FlywheelVelocityRPM", measuredSpeed);
        // SmartDashboard.putNumber("Flywheel Encoder ", )

    } // output

    // =========== continuous ==========
    @Override
    public void disabledContinuous() {
    }

    @Override
    public void autoContinuous() {
    }

    @Override
    public void teleopContinuous() {
    }

    // =========== others ===========
    @Override
    public void smartDashboard() {

    } // display all this to smart dashboard

    public static synchronized Shooter getInstance() {
        return instance == null ? (instance = new Shooter()) : instance;
    } // getInstance

} // Shooter 