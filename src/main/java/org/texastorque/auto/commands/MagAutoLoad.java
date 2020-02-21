package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;
import org.texastorque.inputs.State.RobotState;

public class MagAutoLoad extends Command {

    boolean lowMagBall;
    boolean highMagBall;
    boolean keepGoing;
    int count;

    public MagAutoLoad() {
        super(0);
    }

    @Override
    protected void init() {
        lowMagBall = false;
        highMagBall = false;
        keepGoing = false;
        count = 0;
    }

    @Override
    protected void continuous() {
        if (state.getRobotState() == RobotState.MAGLOAD){
            keepGoing = true;
        } else {
            keepGoing = false;
        }
        lowMagBall = Feedback.getMagLow();
        highMagBall = Feedback.getMagHigh();
        // doing each case independently so easy to modify
        if (!lowMagBall && !highMagBall){ // if there is no ball anywhere 
            input.setLowMag(true);
        }
        else if (lowMagBall && !highMagBall){ // if there is only a ball in the bottom but not in the top
            input.setLowMag(true);
            input.setHighMag(true);
        }
    }

    @Override
    protected boolean endCondition() { // true ends 
        return !keepGoing;
    }

    @Override
    protected void end() {

    }
}
