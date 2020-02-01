package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.inputs.*;
import org.texastorque.torquelib.controlLoop.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private boolean clockwise = true;

    private TorqueMotor left1 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_1), !clockwise);
    private TorqueMotor left2 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_2), !clockwise);
    private TorqueMotor left3 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_3), !clockwise);
    
    private TorqueMotor right1 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_1), clockwise);
    private TorqueMotor right2 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_2), clockwise);
    private TorqueMotor right3 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_3), clockwise);
    
    private Input input = Input.getInstance();
  
    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;


    private ScheduledPID linePID;
    private LowPassFilter lowPass;

    private void DriveBase(){

    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        
    }

    @Override
    public void teleopInit(){
        leftSpeed = 0;
        rightSpeed = 0;
        linePID = new ScheduledPID.Builder(0, -1, 1, 1)
            .setPGains(0.03)
            .setIGains(0.005)
            .setDGains(0.00008)
            .build();
        lowPass = new LowPassFilter(0.2);
    }

    @Override 
    public void disabledInit(){

    }
    private double pidValue;
    private double position;
    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        SmartDashboard.putNumber("left", leftSpeed);
        SmartDashboard.putNumber("right", rightSpeed);
        if (state == RobotState.VISION){
            feedback.shooterMode();
            SmartDashboard.putNumber("hOffset", Feedback.getHOffset());
            position = lowPass.filter(-Feedback.getHOffset());
            pidValue = linePID.calculate(position);
            leftSpeed = pidValue;
            rightSpeed = -pidValue;
        }
        output();
    }

    @Override 
    public void output(){
        left1.set(leftSpeed);
        left2.set(leftSpeed);
        left3.set(leftSpeed);
        right1.set(rightSpeed);
        right2.set(rightSpeed);
        right3.set(rightSpeed);
    }

    // =========== continuous ==========
    @Override
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override
    public void teleopContinuous(){}

    // =========== others ===========
    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static DriveBase getInstance(){
        if (instance == null){
            synchronized(DriveBase.class){
                if (instance == null)
                    instance = new DriveBase();
            }
        }
        return instance;
    } // getInstance

} // Drivebase
