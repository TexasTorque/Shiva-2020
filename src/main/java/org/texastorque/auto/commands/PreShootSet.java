package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.State;
import org.texastorque.inputs.State.RobotState;

public class PreShootSet extends Command {

    boolean keepGoing = false;

    boolean lowMag = true;
    boolean highMag = true;
    boolean gate = true;

    boolean runMag = true;
    boolean lowMagEnded = false;
    boolean highMagEnded = false;
    boolean gateEnded = false;

    double startTime;

    private State state;

    public PreShootSet() {
        super(0);
    }

    @Override
    protected void init() {
        state = State.getInstance();
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        lowMagEnded = false;
        highMagEnded = false;
        gateEnded = false;
    }

    @Override
    protected void continuous() {
        double timeChanged = edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime;
        if (state.getRobotState() == RobotState.SHOOTING){
            keepGoing = true;
        } else {
            keepGoing = false;
        }
        if (timeChanged > 0.25){
            input.setLowMag(true);
            input.setHighMag(true);
        } // running magazine outputs 
        input.setGate(true);
    } // continuous 

    @Override
    protected boolean endCondition() {
        return (!keepGoing);
    } // end condition 

    @Override
    protected void end() {
        input.setLowMag(false);
        input.setHighMag(false);
        input.setGate(false);
        feedback.resetCount();
    }
}
