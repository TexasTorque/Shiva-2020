package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;

public class VisionShoot extends Command {

    private double time;
    private double startTime;
    private boolean targetPresent;
    private double distanceAway;
    private double flywheelSpeed;
    private int hoodSetpoint;

    public VisionShoot(double delay, double time, double backupVelocity, int backupHoodSetpoint) {
        super(delay);
        this.time = time;
        flywheelSpeed = backupVelocity;
        hoodSetpoint = backupHoodSetpoint;
    }

    @Override
    protected void init() {
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
    }

    @Override
    protected void continuous() {
        if (Feedback.getYOffset() != 0) {targetPresent = true;} else {targetPresent = false;}
        if (targetPresent){
            distanceAway = Feedback.getDistanceAway();
            // shoot everything = the whole sequence of events required in order to shoot 
            // flywheelSpeed = 5565.9 + 9.556*distanceAway - 0.735*Math.pow(distanceAway, 2) + 0.009*Math.pow(distanceAway, 3) - 0.00003*Math.pow(distanceAway, 4);
            // flywheelSpeed = 3925 + 51.84663*distanceAway - 3.67*Math.pow(distanceAway,2) + 0.1085119*Math.pow(distanceAway,3) - 0.0009953746*Math.pow(distanceAway, 4);
            hoodSetpoint = 3;
        }
        // input.setFlywheelSpeed(flywheelSpeed);
        input.setHoodSetpoint(hoodSetpoint);
    } // continuous 

    @Override
    protected boolean endCondition() {
        if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime > time){
            return true;
        }
        return false;
    } // end condition

    @Override
    protected void end() {
        input.setFlywheelSpeed(0);
        input.setHoodSetpoint(0);
    }
}
