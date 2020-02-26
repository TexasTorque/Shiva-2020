package org.texastorque.subsystems;

import org.texastorque.constants.Ports;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.component.TorqueSparkMax;

import edu.wpi.first.wpilibj.Servo;

public class Climber extends Subsystem {

    private static volatile Climber instance;

    // ========== motors ==========
    private TorqueSparkMax climberLeft = new TorqueSparkMax(Ports.CLIMBER_LEFT);
    private TorqueSparkMax climberRight = new TorqueSparkMax(Ports.CLIMBER_RIGHT);

    private Servo leftRatchet = new Servo(1);
    private Servo rightRatchet = new Servo(2);

    // =========== variables ============
    private int climbStatus = 0;
    private boolean notStarted = true;
    private boolean inReverse = false;
    private double startTime = 0;

    private double climberLeftSpeed = 0;
    private double climberRightSpeed = 0;
    
    private double leftRatchetPos = 0;
    private double rightRatchetPos = 1;

    // =================== methods and important stuff ================
    public Climber(){
    }

    @Override
    public void autoInit() {
    }

    @Override
    public void teleopInit() {
        climberLeftSpeed = 0;
        climberRightSpeed = 0;
        leftRatchetPos = 0;
        rightRatchetPos = 0;
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledContinuous() {}

    @Override
    public void autoContinuous() {}

    @Override
    public void teleopContinuous() {}

    @Override
    public void run(RobotState state) {
        state = input.getState();
        if (state == RobotState.AUTO){
            climberLeftSpeed = 0;
            climberRightSpeed = 0;
            leftRatchetPos = 0;
            rightRatchetPos = 0;
        }
        if(state == RobotState.TELEOP){
            // climberLeftSpeed = input.getClimberLeft();
            // climberRightSpeed = input.getClimberRight();
            climbStatus = input.getClimberStatus();
            switch (climbStatus){
                case -1: // retract climber 
                    climberLeftSpeed = 0.3;
                    climberRightSpeed = - 0.3;
                    break;
                case 0: // do nothing 
                    climberLeftSpeed = 0;
                    climberRightSpeed = 0;
                    break;
                case 1: // extend climber
                    if (notStarted){
                        leftRatchetPos = - 0.5;
                        rightRatchetPos = 0.5; 
                        notStarted = false;
                        inReverse = true;
                        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                    } // unlocking ratchets 
                    else if (inReverse){
                        if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < 1){
                            climberLeftSpeed = 0.1;
                            climberRightSpeed = - 0.1;
                        }
                        else {
                            inReverse = false;
                        }
                    } // bringing climb back 
                    else {
                        climberLeftSpeed = - 0.3;
                        climberRightSpeed = 0.3;
                    } // climbing 
            } // climb status switch 
        } // if in teleop 
        output();
    }

    @Override
    protected void output() {
        climberLeft.set(climberLeftSpeed);
        climberRight.set(climberRightSpeed);
        rightRatchet.set(rightRatchetPos);
        leftRatchet.set(leftRatchetPos);
    }

    @Override
    public void smartDashboard() {}

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
