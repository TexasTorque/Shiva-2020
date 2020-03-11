package org.texastorque.subsystems;

import org.texastorque.constants.Ports;
import org.texastorque.inputs.Feedback;
import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.component.TorqueSparkMax;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem {

    private static volatile Climber instance;

    // ========== motors ==========
    private TorqueSparkMax climberLeft = new TorqueSparkMax(Ports.CLIMBER_LEFT);
    private TorqueSparkMax climberRight = new TorqueSparkMax(Ports.CLIMBER_RIGHT);

    private Servo leftRatchet = new Servo(0);
    private Servo rightRatchet = new Servo(1);

    // =========== variables ============
    private int climbStatus = 0;
    private static boolean notStarted = true;
    private static boolean inReverse = false;
    private double startTime = 0;

    private double climberLeftSpeed = 0;
    private double climberRightSpeed = 0;
    private double climberLeftPos = 0;
    private double climberRightPos = 0;

    // private double leftRatchetPos = 1;
    // private double rightRatchetPos = 0.5;
    private double leftRatchetPos = 0.75;
    private double rightRatchetPos = -0.75;

    // =================== methods and important stuff ================
    public Climber() {
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
    }

    @Override
    public void disabledContinuous() {
    }

    @Override
    public void autoContinuous() {
    }

    @Override
    public void teleopContinuous() {
    }

    public void update(){
    }

    @Override
    public void run(RobotState state) {
        state = input.getState();
        climberLeftPos = climberLeft.getPosition();
        climberRightPos = climberRight.getPosition();
        // if (state == RobotState.AUTO){
        // climberLeftSpeed = 0;
        // climberRightSpeed = 0;
        // leftRatchetPos = 0.5;
        // rightRatchetPos = 0.5;
        // }
        if (state == RobotState.TELEOP) {
            // climberLeftSpeed = input.getClimberLeft();
            // climberRightSpeed = input.getClimberRight();
            leftRatchetPos = 0.38;
            rightRatchetPos = 0.5;
            climbStatus = input.getClimberStatus();
            if (!input.getManualClimb()){
                switch (climbStatus) {
                    case -1: // retract climber
                        climberLeftSpeed = .6;
                        climberRightSpeed = -0.6;
                        break;
                    case 0: // do nothing
                        climberLeftSpeed = 0;
                        climberRightSpeed = 0;
                        break;
                    case 1: // extend climber
                        leftRatchetPos = 0.5;
                        rightRatchetPos = 0.38;
                        if (notStarted) {
                            notStarted = false;
                            inReverse = true;
                            startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                        } // unlocking ratchets
                        else if (inReverse) {
                            if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < .1) {
                                climberLeftSpeed = 0.1;
                                climberRightSpeed = -0.1;
                            } else {
                                inReverse = false;
                            }
                        } // bringing climb back
                        else {
                            if (climberLeftPos > -100){
                                climberLeftSpeed = -1; // -1    
                            }
                            else {
                                climberLeftSpeed = 0;
                            }
                            if (climberRightPos < 100){
                                climberRightSpeed = 1; // 1
                            }
                            else {
                                climberRightSpeed = 0;
                            }
                            // climberLeftSpeed = -1;
                            // climberRightSpeed = 1;
                        } // climbing
                } // climb status switch
            } 
            // 19.625 - right side up inches 56.429 right side up spark max 
            // 21.063 - left side up inches -61.144 left side up spark max 
            else {
                switch(input.sideClimb()){
                    case -1:
                        leftRatchetPos = 0.5;
                        if (notStarted) {
                            notStarted = false;
                            inReverse = true;
                            startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                        } // unlocking ratchets
                        else if (inReverse) {
                            if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < .1) {
                                climberLeftSpeed = 0.1;
                            } else {
                                inReverse = false;
                            }
                        } // bringing climb back
                        else {
                                climberLeftSpeed = -0.3; // -1    
                        } // climbing
                        // climberLeftSpeed = -1;
                        // climberRightSpeed = 0;
                        break;
                    case 0:
                        climberLeftSpeed = 0;
                        climberRightSpeed = 0;
                        break;
                    case 1:
                        rightRatchetPos = 0.38;
                        if (notStarted) {
                            notStarted = false;
                            inReverse = true;
                            startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                        } // unlocking ratchets
                        else if (inReverse) {
                            if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < .1) {
                                climberRightSpeed = -0.1;
                            } else {
                                inReverse = false;
                            }
                        } // bringing climb back
                        else {
                                climberRightSpeed = 0.3; // 1
                        } // climbing
                        // climberLeftSpeed = 0;
                        // climberRightSpeed = 1;
                }
            }
            
        } // if in teleop
        output();
    }

    public static void resetClimb() {
        notStarted = true;
        inReverse = false;
    }

    @Override
    protected void output() {
        climberLeft.set(climberLeftSpeed);
        climberRight.set(climberRightSpeed);
        leftRatchet.set(leftRatchetPos); // TODO
        rightRatchet.set(rightRatchetPos); // TODO
        SmartDashboard.putNumber("left climb pos", climberLeftPos);
        SmartDashboard.putNumber("right climb pos", climberRightPos);
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
