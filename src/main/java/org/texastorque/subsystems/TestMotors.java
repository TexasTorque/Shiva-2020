package org.texastorque.subsystems;

// ============ inputs ===========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.inputs.*;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.torquelib.component.TorqueMotor.ControllerType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// ================== TestMotors ==================
public class TestMotors extends Subsystem{
    private static volatile TestMotors instance;

    // ============ variables =============
    private double speed = 0;
    private double position = 0; 

    // ============ motors ==============
    private TorqueSparkMax testSparkMax = new TorqueSparkMax(Ports.SPARKMAX_PORT_2);

    // =================== methods ==================
    private TestMotors(){
        // testSparkMax.addFollower(Ports.SPARKMAX_PORT_2);
    } // constructor 

    @Override
    public void autoInit(){}

    @Override
    public void teleopInit(){
    } // teleop init

    @Override 
    public void disabledInit(){}

    // ============= actually doing stuff ===========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            // speed = input.getMag();
            speed = -input.getTest();
        }
        output();
    } // run at all times 

    @Override 
    public void output(){
        // testTalon.set(speed);
        testSparkMax.set(speed);
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("velocity", testSparkMax.getVelocity());
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
        SmartDashboard.putNumber("speed", speed);
        SmartDashboard.putNumber("velocity", testSparkMax.getVelocity());
    } // display all this to smart dashboard

    public static TestMotors getInstance(){
        if (instance == null){
            synchronized(TestMotors.class){
                if (instance == null)
                    instance = new TestMotors();
            }
        }
        return instance;
    } // getInstance

} // TestMotors  
