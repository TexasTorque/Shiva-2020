package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.EncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private CANSparkMax left1 = new CANSparkMax(Ports.DB_LEFT_1, MotorType.kBrushless);
    private CANSparkMax left2 = new CANSparkMax(Ports.DB_LEFT_2, MotorType.kBrushless);
    private CANSparkMax right1 = new CANSparkMax(Ports.DB_RIGHT_1, MotorType.kBrushless);
    private CANSparkMax right2 = new CANSparkMax(Ports.DB_RIGHT_2, MotorType.kBrushless);

    private CANEncoder left1_encoder = left1.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder left2_encoder = left2.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder right1_encoder = right1.getEncoder(EncoderType.kHallSensor, 4096);
    private CANEncoder right2_encoder = right2.getEncoder(EncoderType.kHallSensor, 4096);

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;
  
    private boolean clockwise = true;

    private void DriveBase(){
    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){}

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
        left1.set(leftSpeed);
        left2.set(leftSpeed);
        right1.set(rightSpeed);
        right2.set(rightSpeed);
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
