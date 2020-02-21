package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class ShooterSet extends Command {

    public ShooterSet(double delay){ // need to add the automatic calculations for what angle and velocity the ball needs to go
        super(delay);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void continuous() {

    }

    @Override
    protected boolean endCondition() {
        return false;
    }

    @Override
    protected void end() {
        input.setFlywheelSpeed(0);
    }

}
