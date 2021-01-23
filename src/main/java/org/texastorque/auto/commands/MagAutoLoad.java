package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;
import org.texastorque.inputs.State.RobotState;

public class MagAutoLoad extends Command {

    boolean ballLast;
    
    boolean lowMagBall;
    boolean highMagBall;
    
    boolean keepGoing;
    boolean entered;
    double startTime;
    double delayTime;

    int count;

    // make it so that shooting resets the count to zero rather than each time the button is pressed

    public MagAutoLoad() {
        super(0);
    }

    @Override
    protected void init() {
        ballLast = true;

        lowMagBall = true;
        highMagBall = true;
        
        keepGoing = false;
        entered = false;
        delayTime = 0.1; // change this number based on what ends up working best 

        count = 0;
    }
    

    @Override
    protected void continuous() {
        lowMagBall = Feedback.getMagLow();
        highMagBall = Feedback.getMagHigh();

        if (state.getRobotState() == RobotState.MAGLOAD){
            keepGoing = true;
        } else {
            keepGoing = false;
        }

        if (ballLast != lowMagBall){
            if (!lowMagBall){
                count++;
            }
            ballLast = lowMagBall;
        } // should count how many balls have started to be read through the lower sensor 
        System.out.println(count);

        // doing each case independently so easy to modify
        if (count == 0){
            input.setLowMag(true);
            input.setHighMag(true);
        }

        if (count == 1){ // after first ball passes through 
            if (highMagBall){
                input.setLowMag(true);
                input.setHighMag(true);
            }
            else {
                input.setHighMag(false);
                input.setLowMag(true);
            }
        } // ball 1

        if (count == 2){ // after the second ball passes in
            if (highMagBall){
                input.setLowMag(true);
                input.setHighMag(true);
            }
            else {
                input.setHighMag(false);
                input.setLowMag(true);
            }
        } // ball 2

        if (count == 3){ // after it reads the third ball
            if (!entered){
                startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                entered = true;
            }
            if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < delayTime){
                input.setLowMag(true);
            } 
            else{
                input.setLowMag(false);
                keepGoing = false;
            }
        } // ball 3

    } // continuous 

    @Override
    protected boolean endCondition() { // true ends 
        return !keepGoing;
    }

    @Override
    protected void end() {
        input.setLowMag(false);
        input.setLowMag(false);
    }
}
