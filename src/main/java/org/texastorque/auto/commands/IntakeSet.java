package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class IntakeSet extends Command{

    int rotaryPos;


    public IntakeSet(double delay, int rotaryPos){
        super(delay);
        this.rotaryPos = rotaryPos;
    }

    @Override
    protected void init() {
        input.setRotaryPosition(rotaryPos);

    }

    @Override
    protected void continuous() {
        input.setRollerSpeed(.8);
    }

    @Override
    protected boolean endCondition() {
        // if (){

        // }
        return false;
    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub

    }

}
