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

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    double flywheelSpeed = 6000 * Constants.RPM_VICTORSPX_CONVERSION;
    // double flywheelSpeed = 4000*Constants.RPM_VICTORSPX_CONVERSION;
    double flywheelPercent = 1;

    // =========== motors ============
    TalonSRX talonLead = new TalonSRX(Ports.FLYWHEEL_LEAD);
    TalonSRX talonFollower = new TalonSRX(Ports.FLYWHEEL_FOLLOW);
 
    private final PIDConfigurator configurator;

    private Shooter() {
        final ShooterGainProvider gainProvider = new ShooterGainProvider();
        final ShooterConfiguratorPlugin plugin = new ShooterConfiguratorPlugin(talonLead, talonFollower);
        this.configurator = new PIDConfigurator(gainProvider, plugin);
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

            configurator.update(flywheelSpeed);
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

        configurator.setTarget(flywheelSpeed);
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

    /**
     * Utility class for updating talon SRX configuration for the shooter.
     */
    private static class ShooterConfiguratorPlugin implements IConfiguratorPlugin {

        private final int leadProfileSlot;
        private final TalonSRX lead;
        private final TalonSRX follower;

        private boolean didGoBelowSetpoint = false;

        public ShooterConfiguratorPlugin(int profileSlot, TalonSRX lead, TalonSRX follower) {
            this.leadProfileSlot = profileSlot;
            this.lead = lead;
            this.follower = follower;
        }

        public ShooterConfiguratorPlugin(TalonSRX lead, TalonSRX follower) {
            this(0, lead, follower);
        }

        @Override
        public void initialize() {
            lead.selectProfileSlot(this.leadProfileSlot, 0);
            lead.set(ControlMode.Velocity, 0);
            follower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
            
            lead.setNeutralMode(NeutralMode.Coast);
            follower.setNeutralMode(NeutralMode.Coast);
        }

        @Override
        public void updateGains(KPIDGains gains) {
            lead.config_kF(this.leadProfileSlot, gains.k);
            lead.config_kP(this.leadProfileSlot, gains.p);
            lead.config_kI(this.leadProfileSlot, gains.i);
            lead.config_kD(this.leadProfileSlot, gains.d);

            didGoBelowSetpoint = false;
        }

        @Override
        public void setOutputTarget(double speed) {
            boolean shouldCoast = lead.getSelectedSensorVelocity() > speed;            
            SmartDashboard.putBoolean("Coasting", shouldCoast); 
            
            if (shouldCoast && !didGoBelowSetpoint) {
                // lead.set(ControlMode.Velocity, 0);
                lead.neutralOutput();
                // lead.setNeutralMode(NeutralMode.Coast);
                // follower.setNeutralMode(NeutralMode.Coast);
            } else if (!shouldCoast) {
                didGoBelowSetpoint = true;
                lead.set(ControlMode.Velocity, speed);
            }
            follower.set(ControlMode.Follower, Ports.FLYWHEEL_LEAD);
        }
    }

    /**
     * Utility class to calculate PID gains for the shooter.
     */
    private static class ShooterGainProvider implements IGainProvider {

        final KPIDGains pidHigh = new KPIDGains(0.00902, 0.2401, 0, 5);
        final KPIDGains pidLow = new KPIDGains(0.00902, 0.08, 0, 8);
    
        @Override
        public KPIDGains provide(double setpoint) {
            final var isHigh = setpoint > 5000 * Constants.RPM_VICTORSPX_CONVERSION;
            return isHigh ? pidHigh : pidLow;
        }
    };

} // Shooter 
