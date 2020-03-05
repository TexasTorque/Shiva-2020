package org.texastorque.subsystems;

// ========= imports =========
import org.texastorque.inputs.State.RobotState;
import org.texastorque.constants.*;
import org.texastorque.torquelib.component.TorqueMotor;
import org.texastorque.torquelib.component.TorqueSparkMax;
import org.texastorque.util.KPID;
import org.texastorque.inputs.Feedback;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;

// ======== ColorMuncher =========
public class ColorMuncher extends Subsystem{
    private static volatile ColorMuncher instance;

    KPID kPIDRotary_colorMunch = new KPID(0.07, 0.00005, 0.00002, 0, -.8, .8);
    private double rotaryPosition_Color = 0;

    private int thechosen;

    private TorqueMotor armBend;
    private TorqueMotor wheelTurn;

    private TorqueSparkMax rotary_Color = new TorqueSparkMax(Ports.COLORMUNCH_ROTARY);

    private void ColorMuncher(){
        rotary_Color.configurePID(kPIDRotary_colorMunch);
        rotary_Color.tareEncoder();
    } // constructor 

    // ============= initialization ==========
    @Override 
    public void autoInit(){
    }

    @Override
    public void teleopInit(){
        rotary_Color.tareEncoder();
    }

    @Override 
    public void disabledInit(){
    }

    // ============ actually doing stuff ==========
    @Override 
    public void run(RobotState state){
        if (state == RobotState.TELEOP){
            rotaryPosition_Color = input.getRotaryPositionColor(); //make
            
            SmartDashboard.putNumber("rotary_Color.position", rotary_Color.getPosition());
            SmartDashboard.putNumber("rotaryPosition_Color", rotaryPosition_Color);
        }
        output();
    }

    @Override 
    public void output(){
        SmartDashboard.putNumber("output_color_current", rotary_Color.getCurrent());
    //    testSparkMax.set(speed);
    }

    // =========== continuous ==========
    //counter for how many different colors it has seen
    private int colorCounter = 0;
    @Override
    public void disabledContinuous(){}

    @Override 
    public void autoContinuous(){}

    @Override
    public void teleopContinuous(){
    
    }

    // =========== Get Methods! ==========
    public double getRotaryPosColor(){
        return rotary_Color.getPosition();
    }

    // =========== others ===========
    @Override 
    public void smartDashboard(){

    } // display all this to smart dashboard

    public static ColorMuncher getInstance(){
        if (instance == null){
            synchronized(ColorMuncher.class){
                if (instance == null)
                    instance = new ColorMuncher();
            }
        }
        return instance;
    } // getInstance

} // Drivebase