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

    private int lowMagFlo = 0;
    private int highMagFlo = 0;
    private boolean entered = false;
    private double startTime;
    private final double DELAY = 0.1;

    private double magSpeed_low = -.6; // keep this number positive
    private double magSpeed_high = .5; // keep this number positive

    // ============ motors ==============
    private TorqueSparkMax beltHigh = new TorqueSparkMax(Ports.BELT_HIGH);
    private TorqueSparkMax beltLow = new TorqueSparkMax(Ports.BELT_LOW);
    private TorqueSparkMax beltGate = new TorqueSparkMax(Ports.BELT_GATE);

    // =================== methods ==================
    public Magazine(){
        beltHigh.setAlternateEncoder();
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){} // teleop init

    @Override 
    public void disabledInit(){}

    //updating feedback
    public void update(){
        feedback.setShooterVelocity(beltHigh.getAlternateVelocity());
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
            System.out.println("inside auto mag");
        }
        else if ((state == RobotState.TELEOP || state == RobotState.VISION) && !input.needPreShoot()){
            lowMagFlo = input.getMagLowFlow();
            highMagFlo = input.getMagHighFlow();
            beltSpeed_gate = input.getMagGate();

            beltSpeed_high = 0;
            beltSpeed_low = 0;

            if (highMagFlo == 1 && Feedback.getHighMagPast()){
                beltSpeed_high = 0;
            }
            else if (highMagFlo == 1){
                beltSpeed_high = magSpeed_high;
            }
            else if (highMagFlo == -1){
                beltSpeed_high = - magSpeed_high;   
            } // stops balls from going past high mag
            if (lowMagFlo == 1 && Feedback.getCount() == 3){
                if (!entered){
                    startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
                    entered = true;
                }
                if (edu.wpi.first.wpilibj.Timer.getFPGATimestamp() - startTime < DELAY){
                    beltSpeed_low = magSpeed_low;
                }
                else {
                    beltSpeed_low = 0;
                }
            }
            else if (lowMagFlo == 1){
                beltSpeed_low = magSpeed_low;
            } 
            else if (lowMagFlo == -1) {
                beltSpeed_low = -magSpeed_low;
            }// on the third ball, wait for a bit then stop running the low mag
            System.out.println(Feedback.getCount());
            // beltSpeed_high = input.getMagHigh();
            // beltSpeed_low = input.getMagLow();
            // beltSpeed_gate = input.getMagGate();
            System.out.println("teleop but not preshoot");
        }
        else if ((state == RobotState.TELEOP || state == RobotState.VISION) && input.needPreShoot()){
            feedback.resetCount();
            beltSpeed_high = input.getMagHigh();
            beltSpeed_low = input.getMagLow();
            beltSpeed_gate = input.getMagGate();
            System.out.println("teleop but preshoot");
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
        beltHigh.set(beltSpeed_high); // running raw output rn (maybe add pid later?)
        beltLow.set(beltSpeed_low);
        beltGate.set(beltSpeed_gate);
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