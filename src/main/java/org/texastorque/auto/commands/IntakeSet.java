package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class IntakeSet extends Command{

    int rotaryPos;
    int rollerStatus;

    // all the way up, neutral, down 0 1 2 rotary pos
    // outtake nothing intake -1 0 1 roller status
    public IntakeSet(double delay, int rotaryPos, int rollerStatus){ // UNTESTED
        super(delay);
        this.rotaryPos = rotaryPos;
        this.rollerStatus = rollerStatus;
    }

    @Override
    protected void init() {
        input.setRotaryPosition(rotaryPos, rollerStatus);
    }

    @Override
    protected void continuous() {}

    @Override
    protected boolean endCondition() {
        // if (){

        // }
        // return false;
        return true;
    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub nothing?
    }

}
