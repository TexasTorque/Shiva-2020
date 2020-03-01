package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;

import com.revrobotics.SparkMax;

import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;
import org.texastorque.util.KPID;
import org.texastorque.util.pid.KPIDGains;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ================== Climber ==================
public class Magazine extends Subsystem{
    private static volatile Magazine instance;

    // ============ variables =============
    private KPID beltPID = new KPID(0, 0, 0, 0, -1, 1);
    private double beltSpeed_high = 0;
    private double beltSpeed_low = 0;
    private double beltSpeed_gate = 0;
    private boolean autoMag = true;

    private int robotMultiplier = -1; // if longshot = -1, if bravo = 1

    private int lowMagFlo = 0;
    private int highMagFlo = 0;
    private boolean entered = false;
    private double startTime;
    private final double DELAY = 0.0;
    private boolean preShootStarted = false;
    private boolean startHighMag = false;
    private double startTimeHighMag;
    // private double magSpeed_low = -.6; // keep this number positive
    // private double magSpeed_high = .5; // keep this number positive
    private double magSpeed_low = -.8; // keep this number positive
    private double magSpeed_high = .9; // keep this number positive

    // ============ motors ==============
    private TorqueSparkMax beltHigh = new TorqueSparkMax(Ports.BELT_HIGH);
    private TorqueSparkMax beltLow = new TorqueSparkMax(Ports.BELT_LOW);
    private TorqueSparkMax beltGate = new TorqueSparkMax(Ports.BELT_GATE);

    // =================== methods ==================
    public Magazine(){
        // beltGate.setAlternateEncoder();
        // beltHigh.setAlternateEncoder();
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
        feedback.resetCount();
    } // teleop init

    @Override 
    public void disabledInit(){}

    //updating feedback
    public void update(){
        // feedback.setShooterVelocity(beltGate.getAlternateVelocity());
        // feedback.setShooterVelocity(beltHigh.getAlternateVelocity());
    }
    
    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        update();
        input.updateState();
        beltSpeed_gate = 0;
        if (state == RobotState.AUTO){
            beltSpeed_high = input.getMagHigh();
            beltSpeed_low = input.getMagLow();
            beltSpeed_gate = input.getMagGate();
        }
        else if ((state == RobotState.TELEOP || state == RobotState.VISION) && !input.needPreShoot()){ // auto load mag 
            preShootStarted = false;
            lowMagFlo = input.getMagLowFlow();
            highMagFlo = input.getMagHighFlow();
            beltSpeed_gate = input.getMagGate();

            beltSpeed_high = 0;
            beltSpeed_low = 0;
            if (input.getAutoMagTrue()){
                // if (highMagFlo == 1 && Feedback.getHighMagPast()){
                //     if (!startHighMag){
                //         startTimeHighMag = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                //         startHighMag = true;
                //     }
                //     else if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTimeHighMag < 0){
                //         beltSpeed_high = magSpeed_high;
                //     }
                //     else {
                //         beltSpeed_high = 0;
                //     }
                // }
                // else if (highMagFlo == 1){
                //     beltSpeed_high = magSpeed_high;
                // }
                // else if (highMagFlo == -1){
                //     beltSpeed_high = - magSpeed_high;   
                // } // stops balls from going past high mag // not working code for delay to stop on top

                if (highMagFlo == 1 && Feedback.getMagHigh()){
                    if (!startHighMag){
                        startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                        startHighMag = true;
                    }
                    if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < 0.3){
                        beltSpeed_high = magSpeed_high;
                    }
                    else {
                        beltSpeed_high = 0;
                    }
                }
                else if (highMagFlo == 1){
                    beltSpeed_high = magSpeed_high;
                } 
                else if (highMagFlo == -1) {
                    beltSpeed_high = -magSpeed_high;
                }// on the high ball, wait for a bit then stop running the low mag

                if (lowMagFlo == 1 && Feedback.getCount() >= 3){
                    // if (!entered){
                    //     startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                    //     entered = true;
                    // }
                    // if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < DELAY){
                    //     beltSpeed_low = magSpeed_low;
                    // }
                    // else {
                        beltSpeed_low = 0;
                    // }
                }
                else if (lowMagFlo == 1){
                    beltSpeed_low = magSpeed_low;
                } 
                else if (lowMagFlo == -1) {
                    beltSpeed_low = -magSpeed_low;
                }// on the third ball, wait for a bit then stop running the low mag
            }
            else {
                beltSpeed_high = input.getMagHigh();
                beltSpeed_low = input.getMagLow();
                beltSpeed_gate = input.getMagGate();
            }
            if (beltSpeed_gate != 0){
                startHighMag = false;
                feedback.resetCount();
            }
            
            SmartDashboard.putNumber("count", Feedback.getCount());
            SmartDashboard.putBoolean("autoMag", input.getAutoMagTrue());
            // beltSpeed_high = input.getMagHigh();
            // beltSpeed_low = input.getMagLow();
            // beltSpeed_gate = input.getMagGate();
        }
        else if ((state == RobotState.TELEOP || state == RobotState.VISION) && input.needPreShoot()){ // shooting setup
            startHighMag = false;
            feedback.resetCount();
            if (!preShootStarted){
                startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                preShootStarted = true;
                System.out.println("in start");
            }
            else if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < 0.25){
                beltSpeed_gate = -1;    
                beltSpeed_high = 0;
                beltSpeed_low = 0;
                System.out.println("in gate only");
            }
            else {
                beltSpeed_gate = -1;    
                // beltSpeed_high = 0.5;
                // beltSpeed_low = -0.6;
                beltSpeed_high = 0.8;
                beltSpeed_low = input.getBen();
                System.out.println("in normal");
                System.out.println("ben" + beltSpeed_low);
            }
            // beltSpeed_high = input.getMagHigh();
            // beltSpeed_low = input.getMagLow();
            // beltSpeed_gate = input.getMagGate();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        // input.setFlywheelEncoderSpeed(beltHigh.getAlternateVelocity());
        SmartDashboard.putNumber("HighMagSpeed", beltSpeed_high); // test z axis 
        SmartDashboard.putNumber("LowMagSpeed", beltSpeed_low); // test z axis 
        SmartDashboard.putNumber("GateMagSpeed", beltSpeed_gate);
        SmartDashboard.putBoolean("mag_low_magazine", Feedback.getMagLow());
        SmartDashboard.putBoolean("mag_high_magazine", Feedback.getMagHigh());
        beltHigh.set(beltSpeed_high * robotMultiplier); // running raw output rn (maybe add pid later?)
        beltLow.set(beltSpeed_low);
        beltGate.set(beltSpeed_gate * robotMultiplier); 
    } // output

    // ============= continuous =============
    @Override 
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override 
    public void teleopContinuous(){}

    // ============ others ==========

    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static Magazine getInstance(){
        if (instance == null){
            synchronized(Magazine.class){
                if (instance == null)
                    instance = new Magazine();
            }
        }
        return instance;
    } // getInstance

} // Climber 