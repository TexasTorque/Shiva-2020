package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class ShooterSet extends Command{

    private double rpm;
    private int hoodPos;
    public ShooterSet(double delay, double rpm, int hoodPos){
        super(delay);
        this.rpm = rpm;
        this.hoodPos = hoodPos;

    }

    @Override
    protected void init() {
        input.setFlywheelEncoderSpeed(rpm);
        input.setHoodSetpoint(hoodPos);

    }

    @Override
    protected void continuous() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean endCondition() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	protected void end() {
		// TODO Auto-generated method stub
		
    }
}