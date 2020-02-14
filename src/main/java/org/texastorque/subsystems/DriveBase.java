package org.texastorque.subsystems;

import org.texastorque.inputs.Feedback;
// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;
import org.texastorque.util.KPID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ======== DriveBase =========
public class DriveBase extends Subsystem{
    private static volatile DriveBase instance;

    private TorqueSparkMax db_left = new TorqueSparkMax(Ports.DB_LEFT_1);
    private TorqueSparkMax db_right = new TorqueSparkMax(Ports.DB_RIGHT_1);

    private double leftSpeed = 0.0;
    private double rightSpeed = 0.0;

    private ScheduledPID linePID;
    private LowPassFilter lowPass;

    private double pidValue;
    private double position;

    private double leftDrivePositionZero = 0;
    private double rightDrivePositionZero = 0;
    
    private DriveBase(){
        db_left.addFollower(Ports.DB_LEFT_2);
        db_right.addFollower(Ports.DB_RIGHT_2);
        db_right.setAlternateEncoder();
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
        linePID = new ScheduledPID.Builder(0, -1, 1, 1)
            .setPGains(0.03)
            .setIGains(0.005)
            .setDGains(0.00008)
            .build();
        lowPass = new LowPassFilter(0.2);
    }

    @Override 
    public void disabledInit(){}

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        state = input.getState();
        if (state == RobotState.AUTO){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        else if (state == RobotState.TELEOP){
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        else if (state == RobotState.VISION){
            SmartDashboard.putNumber("hOffset", Feedback.getXOffset());
            position = lowPass.filter(-Feedback.getXOffset());
            pidValue = linePID.calculate(position);
            SmartDashboard.putNumber("pidValueVision", pidValue);
            leftSpeed = pidValue;
            rightSpeed = pidValue;
        }
        output();
    } // run

    @Override 
    public void output(){
        SmartDashboard.putNumber("leftSpeed", leftSpeed);
        SmartDashboard.putNumber("rightspeed", rightSpeed);
        //for spark alternate encoder (flywheel)
        input.setFlywheelEncoderSpeed(db_right.getAlternateVelocity());
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

    // =========== encoders ==========
    public void resetEncoders(){
        leftDrivePositionZero = - db_left.getPosition();
        rightDrivePositionZero = db_right.getPosition();
    }

    // =========== others ===========
    @Override 
    public void smartDashboard(){
        SmartDashboard.putString("State", state.getRobotState().name());
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