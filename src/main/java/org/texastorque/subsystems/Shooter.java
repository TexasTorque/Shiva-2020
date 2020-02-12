package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueTalon;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    private ArrayList<KPID> pidValues = new ArrayList<>();
    KPID kPIDLow = new KPID(0.08, 0, 8, 0.00902, -.5, .5); // tuned for 3000 rpm 
    KPID kPIDHigh = new KPID(0.2401, 0, 5, 0.00902, -.5, .5); // tuned for 6000 rpm 

    double flywheelSpeed = 6000 * Constants.RPM_VICTORSPX_CONVERSION;
    
    // =========== motors ============
    private TorqueTalon flywheel = new TorqueTalon(Ports.FLYWHEEL_LEAD);

    // =========================================== methods ==============================================
    private Shooter() {
        flywheel.addFollower(Ports.FLYWHEEL_FOLLOW);
        // flywheel.invertFollower();
        // pidValues.add(kPIDLow);
        // pidValues.add(kPIDHigh);
        // flywheel.configurePID(pidValues.get(0));
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
        if (state == RobotState.AUTO){
        } // if in autonomous
        if (state == RobotState.TELEOP) {
            flywheelSpeed = input.getFlywheelPercent();
            // flywheelSpeed += input.getFlywheelSpeed();
            // if (flywheelSpeed > 4500){
            //     flywheel.updatePID(pidValues.get(1));
            // } // set to pid high
            // else {
            //     flywheel.updatePID(pidValues.get(0));
            // } // set to pid low
        } // if in teleop
        output();
    } // run at all times

    @Override
    public void output() {
        flywheel.set(flywheelSpeed);
        SmartDashboard.putNumber("flywheel velocity", flywheel.getVelocity());
        // flywheel.set(flywheelSpeed, ControlMode.Velocity);
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