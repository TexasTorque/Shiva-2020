package org.texastorque.subsystems;

import org.texastorque.inputs.Feedback;
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
    // private ScheduledPID shooterPID;
    KPID kPIDLow = new KPID(0.08, 0, 8, 0.00902, -.5, .5); // tuned for 3000 rpm 
    KPID kPIDHigh = new KPID(0.2401, 0, 5, 0.00902, -1, 1); // tuned for 6000 rpm 
    KPID hoodkPID = new KPID( 0.1, 0, 0, 0, -1, 1); //Hood PID for all positions

    private double flywheelSpeed;
    private double flywheelPercent;
    private double pidOutput = 0;
    private double hoodSetpoint;
    
    // =========== motors ============
    // private TorqueTalon flywheel = new TorqueTalon(Ports.FLYWHEEL_LEAD);
    private TorqueSparkMax hood = new TorqueSparkMax(Ports.SHOOTER_HOOD);
    private TorqueSparkMax flywheel = new TorqueSparkMax(Ports.FLYWHEEL);

    // =========================================== methods ==============================================
    private Shooter() {
        //Configuring Motors
        // flywheel.addFollower(Ports.FLYWHEEL_FOLLOW);
        // flywheel.invertFollower();
        hood.configurePID(hoodkPID);
        flywheel.configurePID(kPIDHigh); // INSERT FOR SHOOTER PID
        hood.tareEncoder();
        // pidValues.add(kPIDLow);
        // pidValues.add(kPIDHigh);
        // flywheel.configurePID(pidValues.get(0));

        //Bulding shooter PID (this is for when the encoder is put into the spark max!)
        //Use integrated PID when encoder is put onto talon

        // shooterPID = new ScheduledPID.Builder(0, -1, 1, 1)
        //     .setPGains(0.028)
        //     .setIGains(0.0008)
        //     .setDGains(0)
        //     .setFGains(.00385)
        //     .build();
    } // constructor

    // ============= initialization ==========

    @Override
    public void autoInit() {
    } // autoInit

    @Override
    public void teleopInit() {
    } // teleopInit

    @Override
    public void disabledInit() {} // disabledInit

    //updating feedback
    public void update(){
        //setting flywheel velocity is in magazine because the encoder is attached to magazine SparkMax
        feedback.setHoodPosition(hood.getPosition());
        feedback.setShooterVelocity(flywheel.getVelocityConverted());
        SmartDashboard.putNumber("converted velocity", flywheel.getVelocityConverted());
    }
    // ============ actually doing stuff ==========

    //conversion from RPM to encoders arbitrary units (1/32) (NEED TO GET FOR SPARK MAX)
    private double tempConversionSpark = -.03125;

    @Override
    public void run(RobotState state) {
        update();
        input.setFlywheelOutputType(true);
        // if (state == RobotState.AUTO){
        //     flywheelPercent = input.getFlywheelPercent();
        //     hoodSetpoint = input.getHoodSetpoint();
        //     flywheelSpeed = input.getFlywheelSpeed()*tempConversionSpark;
        //     shooterPID.changeSetpoint(flywheelSpeed);
        //     pidOutput = shooterPID.calculate(feedback.getShooterVelocity());
        // } // if in autonomous
        // if (state == RobotState.TELEOP || state == RobotState.VISION || state == RobotState.SHOOTING || state == RobotState.MAGLOAD) {
        //     //====================Flywheel====================
        //     //When Encoder is in Spark Max!
        //         flywheelPercent = input.getFlywheelPercent();
        //         hoodSetpoint = input.getHoodSetpoint();
        //         pidOutput = input.getFlywheelSpeed();
        //         flywheelSpeed = input.getFlywheelSpeed()*tempConversionSpark;
        //         shooterPID.changeSetpoint(flywheelSpeed);
        //         pidOutput = shooterPID.calculate(feedback.getShooterVelocity());
        //     //================Hood==============
        // } // if in teleop
        flywheelPercent = input.getFlywheelPercent();
        hoodSetpoint = input.getHoodSetpoint();
        flywheelSpeed = input.getFlywheelSpeed();
        // flywheelSpeed = input.getFlywheelSpeed()*tempConversionSpark;
        // shooterPID.changeSetpoint(flywheelSpeed);
        // pidOutput = shooterPID.calculate(feedback.getShooterVelocity());
        output();
    } // run at all times

    @Override
    public void output() {
        hood.set(hoodSetpoint, ControlType.kPosition);
        SmartDashboard.putNumber("hood output", hood.getCurrent());
        SmartDashboard.putNumber("flywheel percent output", flywheelPercent);
        SmartDashboard.putNumber("flywheel output current", flywheel.getCurrent());
        if (input.getFlywheelPercentMode()){
            flywheel.set(flywheelPercent);
        }
        else {
            if(pidOutput > 0){
                flywheel.set(0);
            } // allows motor to coast rather than fighting motion when slowing down (for Spark configuration)
            else{
                flywheel.set(flywheelSpeed, ControlType.kVelocity);
            }
        }
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
        SmartDashboard.putNumber("flywheel put speed", input.getFlywheelSpeed());
        SmartDashboard.putNumber("flywheel setpoint", flywheelSpeed);
        SmartDashboard.putBoolean("Spun Up", Math.abs(feedback.getShooterVelocity() - flywheelSpeed) < 15); 
        SmartDashboard.putNumber("flywheel velocity", feedback.getShooterVelocity());
        SmartDashboard.putNumber("pidOutput", pidOutput);
        SmartDashboard.putNumber("Hood Position", hood.getPosition());
        SmartDashboard.putNumber("Hood Setpoint", hoodSetpoint);
        SmartDashboard.putNumber("distance away", Feedback.getDistanceAway());
        // SmartDashboard.putNumber("Flywheel RPM",flywheel.getVelocity()/Constants.RPM_VICTORSPX_CONVERSION);
    } // display all this to smart dashboard

    public static synchronized Shooter getInstance() {
        return instance == null ? (instance = new Shooter()) : instance;
    } // getInstance

} // Shooter 