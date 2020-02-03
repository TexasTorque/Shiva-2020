package org.texastorque.auto;

import org.texastorque.inputs.*;

public abstract class Command {
    protected Input input = Input.getInstance();
    protected Feedback feedback = Feedback.getInstance();
    protected State state = State.getInstance();

    private double delay;
    private boolean started;
    private boolean ended;

    protected Command(double delay){
        this.delay = delay;
        started = false;
        ended = false;
    } // constructor

    public double getDelay(){
        return delay;
    } // getDelay

    // runs one loop of command to either change setpoint or update output
    // returns whether or not command is done
    public boolean run(){
        if (ended) {
            return ended;
        }

        // first loop
        if (!started){  
            init();
            started = true;
        }

        // single-loop operations for when Command is running
        continuous();

        if (endCondition()) {
            end();
            ended = true;
        }
        return false;
    } // run

    protected abstract void init();

    protected abstract void continuous();
    
    protected abstract boolean endCondition();
    
    protected abstract void end();
} // Command
