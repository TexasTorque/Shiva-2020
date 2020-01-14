package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private TorqueMotor left1;
    private TorqueMotor left2;
    private TorqueMotor left3;
    private TorqueMotor right1;
    private TorqueMotor right2;
    private TorqueMotor right3;
  
    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;
  
    private boolean clockwise = true;

    private void DriveBase(){
        left1 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_1), !clockwise);
        left2 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_2), !clockwise);
        left3 = new TorqueMotor(new VictorSP(Ports.DB_LEFT_3), !clockwise);
        
        right1 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_1), clockwise);
        right2 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_1), clockwise);
        right3 = new TorqueMotor(new VictorSP(Ports.DB_RIGHT_3), clockwise);
    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        
    }

    @Override
    public void teleopInit(){
        leftSpeed = 0;
        rightSpeed = 0;
    }

    @Override 
    public void disabledInit(){

    }

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
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