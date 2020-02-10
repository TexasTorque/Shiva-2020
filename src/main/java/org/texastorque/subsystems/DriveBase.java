package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.util.KPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private TorqueSparkMax db_left = new TorqueSparkMax(Ports.DB_LEFT_1);
    private TorqueSparkMax db_right = new TorqueSparkMax(Ports.DB_RIGHT_1);

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;

    private DriveBase(){
        db_left.addFollower(Ports.DB_LEFT_2);
        db_right.addFollower(Ports.DB_RIGHT_2);
    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        // db_left. // reset drive encoder  HOW DO YOU DO THIS????
    }

    @Override
    public void teleopInit(){
        leftSpeed = 0;
        rightSpeed = 0;
    }

    @Override 
    public void disabledInit(){}

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.AUTO){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        if (state == RobotState.TELEOP){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        output();
    } // run

    @Override 
    public void output(){
        db_left.set(leftSpeed);
        db_right.set(rightSpeed);
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
