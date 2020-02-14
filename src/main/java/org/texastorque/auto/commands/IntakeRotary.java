package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;

public class IntakeRotary extends Command{

    private int position;
    public IntakeRotary(double delay, int position){
        super(delay);
        this.position = position;
    } // constructor
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

    }


}