package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;

public class DriveTime extends Command{

    private double startTime;
    private double time;
    private double speed;

    public DriveTime(double delay, double time, double speed){
        super(delay);
        this.time = time;
        this.speed = speed;
    } // constructor

    @Override
    protected void init(){
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
    } // on initialization

    @Override 
    protected void continuous(){
        input.setDBLeftSpeed(-speed);
        input.setDBRightSpeed(speed);
    } // continuous

    @Override 
    protected boolean endCondition(){
        return edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime > time;
    } // end condition

    @Override
    protected void end(){
        // input.setDBLeftSpeed(0);
        // input.setDBRightSpeed(0);
    } // end
} // DriveTime
