package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;
import jaci.pathfinder.Pathfinder;

public class DriveTurn extends Command {

    // not tested 
    
    private ScheduledPID turnPID;
    private LowPassFilter lowPass;
    private double targetAngle;
    private double position;

    private double currentYaw;
    private double speed;

    public DriveTurn(double delay, double targetAngle) {
        super(delay);
        this.targetAngle = Pathfinder.boundHalfDegrees(targetAngle);
        // turnPID = new ScheduledPID.Builder(0,0.5,1)
        //     .setPGains(0.015)
        //     .setIGains(0.0005)
        //     .build();
        // lowPass = new LowPassFilter(0.5);
        turnPID = new ScheduledPID.Builder(targetAngle, 0.8)
            .setPGains(0.005)
            .setIGains(0.1)
            .build();
    }

    @Override
    protected void init() {
        feedback.resetDriveEncoders();
        feedback.zeroYaw();
    }

    @Override
    protected void continuous() {
        currentYaw = -feedback.getYaw(); // navX yaw is (+) going CW
        // double reverseYaw = currentYaw - Math.signum(currentYaw) * 360;
        
        // double currentError = Math.abs(targetAngle - currentYaw);
        // double reverseError = Math.abs(targetAngle - reverseYaw);
        // double effectiveYaw = currentError < reverseError ? currentYaw : reverseYaw;

        // speed = turnPID.calculate(effectiveYaw);
        // position = lowPass.filter(currentYaw);
        // speed = turnPID.calculate(position);
        speed = turnPID.calculate(currentYaw);

        input.setDBLeftSpeed(speed);
        input.setDBRightSpeed(speed);
    }

    @Override
    protected boolean endCondition() {
        return Math.abs(targetAngle - currentYaw) < 3.0 && Math.abs(feedback.getLeftVelocityDT()) < 0.5;
    }

    @Override
    protected void end() {
        input.setDBLeftSpeed(0);
        input.setDBRightSpeed(0);
    }
}