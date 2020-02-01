package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private CANSparkMax sparkLeft1 = new CANSparkMax(1, MotorType.kBrushless);
    private CANSparkMax sparkLeft2 = new CANSparkMax(2, MotorType.kBrushless);
    private CANEncoder leftGearbox = new CANEncoder(sparkLeft1);

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;
  
    private boolean clockwise = true;

    private void DriveBase(){
        leftGearbox.setPositionConversionFactor(Constants.DRIVE_TRAIN_CONVERSION);
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
        sparkLeft1.set(0);
        sparkLeft2.set(0);
   
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
        SmartDashboard.putNumber("DT Encoder", leftGearbox.getPosition());
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
