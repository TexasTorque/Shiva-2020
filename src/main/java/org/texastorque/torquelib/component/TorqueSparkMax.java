package org.texastorque.torquelib.component;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.util.KPID;

public class TorqueSparkMax extends TorqueMotor {

    private CANSparkMax sparkMax;
    private CANEncoder sparkMaxEncoder;
    private ArrayList<CANSparkMax> sparkMaxFollowers = new ArrayList<>();

    // ===================== constructor stuff =====================
    public TorqueSparkMax(int port) {
        this.port = port;
        sparkMax = new CANSparkMax(port, MotorType.kBrushless);
        sparkMaxEncoder = sparkMax.getEncoder();
    } // constructor 

    @Override
    public void addFollower(int port) {
        sparkMaxFollowers.add(new CANSparkMax(port, MotorType.kBrushless));
        System.out.println("Added spark max follower");
    } // add follower 

    // ===================== set methods ==========================

    @Override
    public void set(double output){
        sparkMax.set(output);
        for(CANSparkMax canSparkMax : sparkMaxFollowers){
            canSparkMax.follow(sparkMax);
        } // takes care of followers
    } // raw set method (-1 to 1)

    public void set(double output, ControlType ctrlType){
        try{
            pid.setReference(output, ctrlType);
            for(CANSparkMax follower : sparkMaxFollowers){
                follower.follow(sparkMax, invert);
            } // takes care of followers
        } catch (Exception e){
            System.out.println(e);
            System.out.println("You need to configure the PID");
        } // try catch
    } // set method for use with PID, position or velocity

    // ===================== PID stuff ========================
    private CANPIDController pid;
    
    @Override
    public void configurePID(KPID kPID) {
        pid = sparkMax.getPIDController();
        pid.setP(kPID.p());
        pid.setI(kPID.i());
        pid.setD(kPID.d());
        pid.setFF(kPID.f());
        pid.setOutputRange(kPID.min(), kPID.max());
    } // configure PID 

    @Override
    public void updatePID(KPID kPID) {
        pid.setP(kPID.p());
        pid.setI(kPID.i());
        pid.setD(kPID.d());
        pid.setFF(kPID.f());
        pid.setOutputRange(kPID.min(), kPID.max());
    } // update PID 

    @Override
    public double getVelocity() {
        return sparkMaxEncoder.getVelocity()* sparkMaxEncoder.getVelocityConversionFactor();
    } // returns velocity of motor 

    @Override
    public double getPosition() {
        return sparkMaxEncoder.getPosition() * sparkMaxEncoder.getPositionConversionFactor();
    } // returns position of motor 

}
