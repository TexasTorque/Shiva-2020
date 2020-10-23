package org.texastorque.subsystems;

import org.texastorque.inputs.State.RobotState;
import org.texastorque.torquelib.component.TorqueSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import org.texastorque.torquelib.component.TorqueSparkMax;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;

import org.texastorque.constants.Ports;

import org.texastorque.inputs.*;

public class ControlPanel extends Subsystem {
    private static volatile ControlPanel instance;    

    private TorqueSparkMax cpMotor = new TorqueSparkMax(Ports.CP_MOTOR_PORT);
    private boolean controlPanelStart = false;
    private double cpMotorSpeed = .5;
    private boolean controlPanelEnd = false;

    private ArrayList <String> colorOrder = new ArrayList <String>();
    
    private ControlPanel() {
        colorOrder.add("red");
        colorOrder.add("green");
        colorOrder.add("blue");
        colorOrder.add("yellow");
        colorOrder.add("red");
        colorOrder.add("green");
        colorOrder.add("blue");
        colorOrder.add("yellow");
    }

    @Override
    public void autoInit() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void teleopInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disabledInit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disabledContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void autoContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void teleopContinuous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void smartDashboard() {
        // TODO Auto-generated method stub

    }

    @Override
    public void run(RobotState state) {
        // TODO Auto-generated method stub
        controlPanelStart = input.getControlPanelStart();
        //get what color its on right now
        //check if it has sensed that color eight times
        //if it has then colorPanelEnd = true
        //phase 1 done
        //check if right dpad is pressed
        //start phase 2
        //keep spinning until color is sensed
        
        
        
    }

    @Override
    protected void output() {
        // TODO Auto-generated method stub
        if (controlPanelStart) {
            if (controlPanelEnd) {
                cpMotor.set(0);
            }
            cpMotor.set(cpMotorSpeed);
            
        }  
        else {
            cpMotor.set(0);
        }

    }
    public static ControlPanel getInstance(){
        if (instance == null){
            synchronized(Magazine.class){
                if (instance == null)
                    instance = new ControlPanel();
            }
        }
        return instance;
    } // getInstance

}