package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class SetRotary extends Command{

    private int position;
    private boolean endCondition = false;
    public SetRotary(double delay, int position){
        super(delay);
        this.position = position;
    } // constructor
    @Override
    protected void init() {    
        input.setRotaryPosition(position);
    }

    @Override
    protected void continuous() {
        endCondition = false;
    }

    @Override
    protected boolean endCondition() {

        return endCondition;
    }

    @Override
    protected void end() {
    }


}