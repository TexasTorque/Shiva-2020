package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;

public class DriveVision extends Command {

    double driveTime;
    double currentOffset;
    double position;
    double pidCalc;
    private ScheduledPID visionPID;
    private LowPassFilter lowPass;

    protected DriveVision(double delay) {
        super(delay);
        currentOffset = 0;
        visionPID = new ScheduledPID.Builder(0,0.5,1)
            .setPGains(0.015)
            .setIGains(0.0005)
            .build();
        lowPass = new LowPassFilter(0.5);
    }

    @Override
    protected void init() {}

    @Override
    protected void continuous() {
        currentOffset = -Feedback.getXOffset();
        position = lowPass.filter(currentOffset);
        pidCalc = visionPID.calculate(position);
        input.setDBLeftSpeed(pidCalc);
        input.setDBRightSpeed(pidCalc);
    }

    @Override
    protected boolean endCondition() {
        if (Math.abs(currentOffset) < 0.1){ return true;}
        return false;
    }

    @Override
    protected void end() {
        input.setDBLeftSpeed(0);
        input.setDBRightSpeed(0);
    }
}
