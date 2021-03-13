package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.*;
public class AutoMag extends Command {

    private double velocity;
    private double time;
    private int hoodSetpoint;
    private double startTime;
    private Feedback feedback = Feedback.getInstance();
    private Input input = Input.getInstance();

    private double beltSpeed_high = 0;
    private double beltSpeed_low = 0;
    private double magSpeed_high = .7;
    private double magSpeed_low = -.5;

    public AutoMag(){ // need to add the automatic calculations for what angle and velocity the ball needs to go
        super(0);
    }

    @Override
    protected void init() { 
        input.setHighMag(true);
        input.setLowMag(true);

    }

    @Override
    protected void continuous() {
        if ((Feedback.magHighCheck.get()) && (Feedback.magLowCheck.get()) && Feedback.getCount() == 3) { // cleanup
            //beltHigh.set(0);
            input.setHighMag(false);
            input.setLowMag(false);
        }
        
        else if (Feedback.magHighCheck.get()) {
            input.setHighMag(false);
            beltSpeed_high = 0;

            
        }
        
        else {
            input.setHighMag(true);
            input.setLowMag(true);
        }
    }

    @Override
    protected boolean endCondition() {
        return ((Feedback.magHighCheck.get()) && (Feedback.magLowCheck.get()) && Feedback.getCount() == 3);
    }

    @Override
    protected void end() {
        
    }

}

