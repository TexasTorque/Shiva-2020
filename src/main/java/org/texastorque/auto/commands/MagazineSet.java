package org.texastorque.auto.commands;

import org.texastorque.auto.Command;

public class MagazineSet extends Command {
    boolean lowMag = false;
    boolean highMag = false;
    boolean gate = false;

    boolean runMag = false;
    boolean lowMagEnded = false;
    boolean highMagEnded = false;
    boolean gateEnded = false;

    double delayMag = 0;
    double lowTime = 0;
    double highTime = 0;
    double gateTime = 0;

    double startTime;

    public MagazineSet(double delay, double delayMag, boolean lowMag, double lowTime, boolean highMag, double highTime, boolean gate, double gateTime) {
        super(delay);
        this.delayMag = delayMag;
        this.lowMag = lowMag;
        this.highMag = highMag;
        this.gate = gate;
        this.lowTime = lowTime;
        this.highTime = highTime;
        this.gateTime = gateTime;
        lowMagEnded = false;
        highMagEnded = false;
        gateEnded = false;
    }

    @Override
    protected void init() {
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        lowMagEnded = false;
        highMagEnded = false;
        gateEnded = false;
    }

    @Override
    protected void continuous() {
        if (runMag){
            if (!lowMagEnded && lowMag){
                input.setLowMag(true);
            }
            else {
                input.setLowMag(false);
            }
            if (!highMagEnded && highMag){
                input.setHighMag(true);
            }
            else {
                input.setHighMag(false);
            }
        } // running magazine outputs 
        if (!gateEnded && gate){
            input.setGate(true);
        }
        else {
            input.setGate(false);
        }
        updateEndConditions();
    } // continuous 

    private void updateEndConditions(){
        double timeChanged = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime;
        if (timeChanged > delayMag){
            runMag = true;
        }
        if (timeChanged >= lowTime){
            lowMagEnded = true;
        }
        if (timeChanged >= highTime){
            highMagEnded = true;
        }
        if (timeChanged >= gateTime){
            gateEnded = true;
        }
    } // update end conditions 

    @Override
    protected boolean endCondition() {
        return (lowMagEnded && highMagEnded && gateEnded);
    } // end condition 

    @Override
    protected void end() {
        input.setLowMag(false);
        input.setHighMag(false);
        input.setGate(false);
    }
}
