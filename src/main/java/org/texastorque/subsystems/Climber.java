package org.texastorque.subsystems;

import org.texastorque.constants.Ports;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.component.TorqueSparkMax;

public class Climber extends Subsystem {

    private static volatile Climber instance;

    private TorqueSparkMax climberLeft = new TorqueSparkMax(Ports.CLIMBER_LEFT);
    private TorqueSparkMax climberRight = new TorqueSparkMax(Ports.CLIMBER_RIGHT);

    private double climberLeftSpeed = 0;
    private double climberRightSpeed = 0;
    
    public Climber(){
    }

    @Override
    public void autoInit() {
    }

    @Override
    public void teleopInit() {
        climberLeftSpeed = 0;
        climberRightSpeed = 0;
    }

    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disabledContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void autoContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void teleopContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void smartDashboard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void run(RobotState state) {
        state = input.getState();
        if(state == RobotState.TELEOP){
            climberLeftSpeed = input.getClimberLeft();
            climberRightSpeed = input.getClimberRight();
        }
        output();
    }

    @Override
    protected void output() {
        climberLeft.set(climberLeftSpeed);
        climberRight.set(climberRightSpeed);
    }

    public static Climber getInstance(){
        if (instance == null){
            synchronized(DriveBase.class){
                if (instance == null)
                    instance = new Climber();
            }
        }
        return instance;
    } // getInstance
}
