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
    double delayTime2;

    double highStartTime;
    boolean highBallStart;

    int count;

    // make it so that shooting resets the count to zero rather than each time the button is pressed

    public MagAutoLoad(double delay) {
        super(delay);
    }

    @Override
    protected void init() {
        ballLast = false;

        lowMagBall = false;
        highMagBall = false;
        
        keepGoing = true;
        entered = false;
        delayTime = 0.2; // change this number based on what ends up working best 
        delayTime2 = 0.1;

        highBallStart = false;

        count = -1;
    }

    @Override
    protected void continuous() {
        lowMagBall = Feedback.getMagLow();
        highMagBall = Feedback.getMagHigh();
        
        // System.out.println(lowMagBall);

        if (ballLast != lowMagBall){
            if (lowMagBall){
                count++;
            }
            ballLast = lowMagBall;
        } // should count how many balls have started to be read through the lower sensor 

        if (!highMagBall){ // no ball in top
            input.setHighMag(true);
        }
        else if (!highBallStart){ // if there is a ball in the top
            highBallStart = true;
            highStartTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        }
        else if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - highStartTime < delayTime){ // delay turning off
            input.setHighMag(true);
        }
        else { // turn off 
            input.setHighMag(false);
        }

        // doing each case independently so easy to modify
        if (count == 0){
            input.setLowMag(true);
            input.setHighMag(true);
        }

        if (count < 2){ // after first ball passes through 
            input.setLowMag(true);
            // if (highMagBall){
            //     input.setLowMag(true);
            //     input.setHighMag(false);
            // }
            // else {
            //     input.setHighMag(true);
            //     input.setLowMag(true);
            // }
        } // ball 1

        if (count == 2){
            keepGoing = false;
        }

        // if (count == 2){ // after the second ball passes in
        //     if (highMagBall){
        //         input.setLowMag(true);
        //         input.setHighMag(false);
        //     }
        //     else {
        //         input.setHighMag(true);
        //         input.setLowMag(true);
        //     }
        // } // ball 2

        if (count == 3){ // after it reads the third ball
            if (!entered){
                startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                entered = true;
            }
            if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < delayTime2){
                input.setLowMag(true);
            } 
            else{
                input.setLowMag(false);
                keepGoing = false;
            }
        } // ball 3
        // else if (count >= 1){ // after first ball passes through 
        //     if (highMagBall){
        //         input.setLowMag(true);
        //         input.setHighMag(false);
        //     }
        //     else {
        //         input.setHighMag(true);
        //         input.setLowMag(true);
        //     }
        // } // ball 1

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
