package org.texastorque.subsystems;

import org.texastorque.inputs.Feedback;
// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.controlLoop.LowPassFilter;
import org.texastorque.torquelib.controlLoop.ScheduledPID;
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
    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
        resetEncoders();
    }

    @Override
    public void teleopInit(){
        leftSpeed = 0;
        rightSpeed = 0;
        linePID = new ScheduledPID.Builder(0, -1, 1, 1)
            .setPGains(0.01)
            .setIGains(.0005)
            // .setIGains(0.0005)
            // .setDGains(0.000005)
            .build();
        lowPass = new LowPassFilter(.5);
    }

    @Override 
    public void disabledInit(){}

    public void update(){
        feedback.setLeftPositionDT(db_left.getPosition());
        feedback.setRightPositionDT(db_right.getPosition());
        feedback.setLeftVelocityDT(db_left.getVelocity());
        feedback.setRightVelocityDT(db_right.getVelocity());
    }
    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        update();
        state = input.getState();
        if (state == RobotState.AUTO){
            input.changeDriveLimelight(true);
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        else if (state == RobotState.TELEOP || state == RobotState.SHOOTING || state == RobotState.MAGLOAD) {
            SmartDashboard.putBoolean("vision", false);
            input.changeDriveLimelight(false);
            Feedback.setLimelightOn(false);
            state = input.getState();
            linePID.reset();
            linePID.setLastError(0);
            SmartDashboard.putNumber("Last Error", linePID.getLastError());
            lowPass.clear();
            leftSpeed = input.getDBLeft();
            rightSpeed = input.getDBRight();
        }
        else if (state == RobotState.VISION){
            input.changeDriveLimelight(true);
            SmartDashboard.putBoolean("vision", true);
            Feedback.setLimelightOn(true);
            state = input.getState();
            SmartDashboard.putNumber("hOffset", Feedback.getXOffset());
            position = lowPass.filter(-Feedback.getXOffset());
            pidValue = - linePID.calculate(position);
            SmartDashboard.putNumber("pidValueVision", pidValue);
            leftSpeed = pidValue;
            rightSpeed = pidValue;
        }
        if (Feedback.getXOffset() < 3){
            SmartDashboard.putBoolean("lined up", true);
        }
        else {
            SmartDashboard.putBoolean("lined up", false);
        }
        output();
    } // run

    @Override 
    public void output(){
        SmartDashboard.putNumber("leftSpeed", leftSpeed);
        SmartDashboard.putNumber("rightspeed", rightSpeed);
        SmartDashboard.putNumber("distance away", Feedback.getDistanceAway());
        SmartDashboard.putBoolean("target visible", Feedback.getYOffset() != 0.000000000);
        // SmartDashboard.putNumber("right drive output", db_right.getCurrent());
        //for spark max alternate encoder (flywheel)
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
        db_left.tareEncoder();
        db_right.tareEncoder();
    } // reset where the zero is of the encoders 

    public double getLeftDistance(){
        return -(db_left.getPosition() - leftDrivePositionZero);
    } // return left drive distance 

    public double getRightDistance(){
        return (db_right.getPosition() - rightDrivePositionZero);
    } // return right drive distance 

    // =========== others ===========

    @Override 
    public void smartDashboard(){
        SmartDashboard.putString("State", state.getRobotState().name());
        SmartDashboard.putNumber("db_right", getRightDistance());
        SmartDashboard.putNumber("db_left", getLeftDistance());
        SmartDashboard.putNumber("y pixels", Feedback.getYOffset());
        SmartDashboard.putNumber("yaw", feedback.getYaw());
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