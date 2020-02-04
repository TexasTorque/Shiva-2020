package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    private ArrayList<Object> pidValues = new ArrayList<Object>();
    double flywheelSpeed = 6000 * Constants.RPM_VICTORSPX_CONVERSION;
    double flywheelPercent = 1;
    KPID kPIDLow = new KPID(0.2401, 0, 5, 0.00902, -.5, .5);

    // =========== motors ============
    private TorqueMotor talonLead = new TorqueMotor(ControllerType.TALONSRX, Ports.FLYWHEEL_LEAD);
    private TorqueMotor talonFollow = new TorqueMotor(ControllerType.TALONSRX, Ports.FLYWHEEL_FOLLOW);

    private Shooter() {
        talonLead.configurePID(kPIDLow);
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
            flywheelSpeed += input.getFlywheelSpeed();
            flywheelPercent += input.getFlywheelPercent();
        } // if in teleop
        output();
    } // run at all times

    @Override
    public void output() {
        double measuredSpeed = talonLead.getVelocity() / Constants.RPM_VICTORSPX_CONVERSION;
        SmartDashboard.putNumber("FlywheelVelocityRPM", measuredSpeed);
        SmartDashboard.putNumber("FlywheelPosition", talonLead.getPosition());
        talonLead.set(flywheelSpeed, ControlMode.Velocity);
        talonFollow.set(Ports.FLYWHEEL_LEAD, ControlMode.Follower);
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

    } // display all this to smart dashboard

    public static synchronized Shooter getInstance() {
        return instance == null ? (instance = new Shooter()) : instance;
    } // getInstance

} // Shooter 