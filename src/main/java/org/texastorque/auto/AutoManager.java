package org.texastorque.auto;

import org.texastorque.auto.commands.MagazineSet;
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
    private String[] autoSequenceNames;
    private SendableChooser<String> autoSelector = new SendableChooser<String>();

    private Sequence currentSequence;
    private boolean sequenceEnded;

    private NetworkTableInstance NT_instance;
    private NetworkTableEntry NT_offsetEntry;

    private AutoManager(){
        autoSequences = new ArrayList<Sequence>();
        // autoSequenceNames = new String[] {"Mode 0", "Baseline", "Mode 2", "Mode 3", "Mode 4", "Testing"};
        autoSequences.add(new VinayMode());
        autoSequences.add(new CrossLine());
        autoSequences.add(new ShootButWorse());
        autoSequences.add(new ShootButNotAsBad());
        autoSequences.add(new Testing());

        // SmartDashboard.putStringArray("AutoList", autoSequenceNames);
        // System.out.println(working);
        autoSelector.setDefaultOption("VinayMode", "VinayMode");
        autoSelector.addOption("CrossLine", "CrossLine");
        autoSelector.addOption("Testing", "Testing");

        SmartDashboard.putData("autos", autoSelector);
        System.out.println("All auto sequences loaded.");
    } // constructor

    public void displayChoices(){
        SmartDashboard.putData("autos", autoSelector);
        SmartDashboard.putStringArray("AutoList", autoSequenceNames);
    } // display choices

    public void chooseSequence(){
        String autoChoice = NetworkTableInstance.getDefault().getTable("SmartDashboard").getSubTable("autos").getEntry("selected").getString("N/A");
        
        System.out.println("autos via network tables: " + autoChoice);

        switch(autoChoice){
            case "VinayMode":
                currentSequence = autoSequences.get(0);
            case "CrossLine":
                currentSequence = autoSequences.get(1);
            case "Testing":
                currentSequence = autoSequences.get(2);
            default: // just change the value in here to test
                currentSequence = autoSequences.get(0);
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

    public void runMagAutomatic(){
        currentSequence = new PreShoot();
        sequenceEnded = false;
    }

    public void runMagLoad(){
        currentSequence = new MagLoad();
        sequenceEnded = false;
    }

    public void resetCurrentSequence(){
        currentSequence.reset();
    } // TESTING 

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
