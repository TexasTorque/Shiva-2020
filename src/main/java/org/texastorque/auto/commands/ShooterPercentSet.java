package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterPercentSet extends Command {

    private double percent;
    private double time;
    private double startTime;
    private int hoodSetpoint;

    public ShooterPercentSet(double delay, double percent, double time, int hoodSetpoint){
        super(delay);
        this.percent = percent;
        this.time = time;
        this.hoodSetpoint = hoodSetpoint;
    }

    @Override
    protected void init() {
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        input.setFlywheelOutputType(true);
        input.setHoodSetpoint(hoodSetpoint);
        System.out.println(percent);
    }

    @Override
    protected void continuous() {
        SmartDashboard.putNumber("got to shooter", percent);
        input.setFlywheelPercent(percent);

    }

    @Override
    protected boolean endCondition() {
        return edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime > time;
    }

    @Override
    protected void end() {
        input.setHoodSetpoint(0);
        input.setFlywheelPercent(0);
        input.setFlywheelOutputType(false);
    }
    
}
