package org.texastorque.auto.commands;

import org.texastorque.auto.Command;
import org.texastorque.inputs.Feedback;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;

public class DriveVision extends Command {

    double driveTime;
    double currentOffset;
    double position;
    double pidCalc;
    double startTime;
    double time;
    private ScheduledPID visionPID;
    private LowPassFilter lowPass;

    public DriveVision(double delay, double time) {
        super(delay);
        currentOffset = 0;
        this.time = time;
        visionPID = new ScheduledPID.Builder(0,0.5,1)
            .setPGains(0.015)
            .setIGains(0.0005)
            .build();
        lowPass = new LowPassFilter(0.5);
    }

    @Override
    protected void init() {
        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
    }

    @Override
    protected void continuous() {
        currentOffset = Feedback.getXOffset();
        position = lowPass.filter(currentOffset);
        pidCalc = visionPID.calculate(position);
        input.setDBLeftSpeed(pidCalc);
        input.setDBRightSpeed(pidCalc);
    }

    @Override
    protected boolean endCondition() {
        if ((Math.abs(currentOffset) < 2 && Math.abs(currentOffset) != 0) || 
        (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime > time)){ 
            return true;
        }
        return false;
    }

    @Override
    protected void end() {
        input.setDBLeftSpeed(0);
        input.setDBRightSpeed(0);
    }
}
