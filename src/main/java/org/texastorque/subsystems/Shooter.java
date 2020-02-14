package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.component.TorqueTalon;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.torquelib.controlLoop.ScheduledPID;
import org.texastorque.util.KPID;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ControlType;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

// ========= Shooter ==========
public class Shooter extends Subsystem {
    private static volatile Shooter instance;

    // ======== variables ==========
    private ArrayList<KPID> pidValues = new ArrayList<>();
    private ScheduledPID shooterPID;
    KPID kPIDLow = new KPID(0.08, 0, 8, 0.00902, -.5, .5); // tuned for 3000 rpm 
    KPID kPIDHigh = new KPID(0.2401, 0, 5, 0.00902, -.5, .5); // tuned for 6000 rpm 
    KPID hoodkPID = new KPID( 0.08, 0, 0, 0, -1, 1);//Hood PID for all positions

    double flywheelSpeed;
    
    // =========== motors ============
    private TorqueTalon flywheel = new TorqueTalon(Ports.FLYWHEEL_LEAD);
    private TorqueSparkMax hood = new TorqueSparkMax(Ports.SHOOTER_HOOD);

    // =========================================== methods ==============================================
    private Shooter() {
        //Configuring Motors
        flywheel.addFollower(Ports.FLYWHEEL_FOLLOW);
        flywheel.invertFollower();
        hood.configurePID(hoodkPID);
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
        //Bulding shooter PID (this is for when the encoder is put into the spark max!)
        //Use integrated PID when encoder is put onto talon
        shooterPID = new ScheduledPID.Builder(0, -1, 1, 1)
            .setPGains(0.002)
            .setIGains(0)
            .setDGains(0)
            .setFGains(.00355)
            .build();
    } // teleopInit

    @Override
    public void disabledInit() {
    } // disabledInit

    // ============ actually doing stuff ==========
    private double pidOutput = 0;
    private double hoodSetpoint;
    //conversion from RPM to encoders arbitrary units (1/32)
    private double tempConversionSpark = -.03125;
    @Override
    public void run(RobotState state) {
        if (state == RobotState.AUTO){
        } // if in autonomous
        if (state == RobotState.TELEOP) {
            //====================Flywheel====================
            //When Encoder is in Spark Max!
                hoodSetpoint = input.getHoodSetpoint();
                flywheelSpeed = input.getFlywheelSpeed()*tempConversionSpark;
                shooterPID.changeSetpoint(flywheelSpeed);
                pidOutput = shooterPID.calculate(input.getFlywheelEncoderSpeed());
            // flywheelSpeed += input.getFlywheelSpeed();
            // if (flywheelSpeed > 4500){
            //     flywheel.updatePID(pidValues.get(1));
            // } // set to pid high
            // else {
            //     flywheel.updatePID(pidValues.get(0));
            // } // set to pid low
            //================Hood==============
        } // if in teleop
        output();
    } // run at all times

    @Override
    public void output() {
        //allows motor to coast rather than fighting motion when slowing down (for Spark configuration)
        if(pidOutput > 0){
            flywheel.set(0);
        }
        else{
            flywheel.set(-pidOutput);
        }
        hood.set(hoodSetpoint, ControlType.kPosition);
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
        SmartDashboard.putNumber("flywheel setpoint", flywheelSpeed);
        SmartDashboard.putNumber("flywheel velocity", input.getFlywheelEncoderSpeed());
        SmartDashboard.putNumber("pidOutput", pidOutput);
        SmartDashboard.putNumber("Hood Position", hood.getPosition());
        SmartDashboard.putNumber("Hood Setpoint", hoodSetpoint);
        // SmartDashboard.putNumber("Flywheel RPM",flywheel.getVelocity()/Constants.RPM_VICTORSPX_CONVERSION);
    } // display all this to smart dashboard

    public static synchronized Shooter getInstance() {
        return instance == null ? (instance = new Shooter()) : instance;
    } // getInstance

} // Shooter 