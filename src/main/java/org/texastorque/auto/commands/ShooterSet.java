package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class ShooterSet extends Command {

    private double velocity;
    private double time;
    private int hoodSetpoint;
    private double startTime;

    public ShooterSet(double delay, double velocity, double time, int hoodSetpoint){ // need to add the automatic calculations for what angle and velocity the ball needs to go
        super(delay);
        this.velocity = velocity;
        this.time = time;
        this.hoodSetpoint = hoodSetpoint;
    }

    @Override
    protected void init() { 
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
    }

    @Override
    protected void continuous() {
        input.setFlywheelSpeed(velocity);
        input.setHoodSetpoint(hoodSetpoint);
    }

    @Override
    protected boolean endCondition() {
        return edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime > time;
    }

    @Override
    protected void end() {
        input.setFlywheelSpeed(0);
        input.setHoodSetpoint(0);
    }

}
