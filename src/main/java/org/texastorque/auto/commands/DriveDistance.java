package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveDistance extends Command {

    ScheduledPID drivePID;
    LowPassFilter lowPass;

    double currentDifference;
    double position;
    double pidCalc;

    double distance;
    
    public DriveDistance(double delay, double distance) { // 196 16.474288
        super(delay);
        drivePID = new ScheduledPID.Builder(0,1,1)
            .setPGains(0.1)
            .setIGains(0.0005)
            .build();
        lowPass = new LowPassFilter(0.5);
        this.distance = distance / 11.9;
    }

    @Override
    protected void init() {
        feedback.resetDriveEncoders();
    }

    @Override
    protected void continuous() {
        currentDifference = distance - Feedback.getDBLeftDistance();
        SmartDashboard.putNumber("leftDistance", Feedback.getDBLeftDistance());
        SmartDashboard.putNumber("rightDistance", Feedback.getDBRightDistance());
        position = lowPass.filter(currentDifference);
        pidCalc = drivePID.calculate(position);
        input.setDBLeftSpeed(pidCalc*.4);
        input.setDBRightSpeed(-pidCalc*.4);
    }

    @Override
    protected boolean endCondition() {
        if (Math.abs(currentDifference) < 2){
            return true;
        }
        return false;
    }

    @Override
    protected void end() {
        input.setDBLeftSpeed(0);
        input.setDBRightSpeed(0);
    }
}