package org.texastorque.auto;

import org.texastorque.auto.sequences.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;
import java.util.Set;

public class AutoManager {
    private static AutoManager instance;

    private ArrayList<Sequence> autoSequences;
    private SendableChooser<String> autoSelector = new SendableChooser<String>();

    private Sequence currentSequence;
    private boolean sequenceEnded;

    private NetworkTableInstance NT_instance;
    private NetworkTableEntry NT_offsetEntry;

    private AutoManager(){
        autoSequences = new ArrayList<Sequence>();
        autoSequences.add(new CrossLine());
        
        autoSelector.setDefaultOption("CrossLine ", "CrossLine");

        SmartDashboard.putData(autoSelector);
        System.out.println("All auto sequences loaded.");
    } // constructor

    public void displayChoices(){
        SmartDashboard.putData(autoSelector);
    } // display choices

    public void chooseSequence(){
        String autoChoice = autoSelector.getSelected();
        System.out.println(autoChoice);

        switch(autoChoice){
            case "CrossLine":
                currentSequence = autoSequences.get(0);
                break;
            default:
                break;
        } // select the autonomous program to run

        currentSequence.reset();
        sequenceEnded = false;
    } // choose sequence

    public void runSequence(){
        currentSequence.run();
        sequenceEnded = currentSequence.hasEnded();
    } // runSequence

    public boolean sequenceEnded(){
        return sequenceEnded;
    } // sequence ended

    public static AutoManager getInstance(){
        if (instance == null){
            synchronized(AutoManager.class){
                if (instance == null)
                    instance = new AutoManager();
            }
        }
        return instance;
    } // getInstance
} // autoManager
