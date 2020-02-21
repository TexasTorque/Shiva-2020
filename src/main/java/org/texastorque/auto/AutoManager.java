package org.texastorque.auto;

import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.sequences.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import java.util.ArrayList;

public class AutoManager {
    private static AutoManager instance;

    private ArrayList<Sequence> autoSequences;
    private String[] autoSequenceNames;
    // private SendableChooser<String> autoSelector = new SendableChooser<String>();

    private Sequence currentSequence;
    private boolean sequenceEnded;

    private AutoManager(){
        autoSequences = new ArrayList<Sequence>();
        autoSequenceNames = new String[] {"Mode 0", "Baseline", "Mode 2", "Testing"};
        autoSequences.add(new VinayMode());
        autoSequences.add(new CrossLine());
        autoSequences.add(new ShootButWorse());
        autoSequences.add(new Testing());

        SmartDashboard.putStringArray("Auto List", autoSequenceNames);
        // System.out.println(working);
        // autoSelector.setDefaultOption("VinayMode", "VinayMode");
        // autoSelector.addOption("CrossLine", "CrossLine");
        // autoSelector.addOption("Testing", "Testing");

        // SmartDashboard.putData(autoSelector);
        System.out.println("All auto sequences loaded.");
    } // constructor

    public void displayChoices(){
        // SmartDashboard.putData(autoSelector);
        SmartDashboard.putStringArray("Auto List", autoSequenceNames);
    } // display choices

    public void chooseSequence(){
        // String autoChoice = autoSelector.getSelected();
        String autoChoice = SmartDashboard.getString("Auto List", "null");
        System.out.println(autoChoice);
        switch(autoChoice){
            case "Mode 0":
                currentSequence = autoSequences.get(0);
                System.out.println("in vinayMode");
                break;
            case "Baseline":
                currentSequence = autoSequences.get(1);
                System.out.println("in crossline");
                break;
            case "Testing":
                currentSequence = autoSequences.get(3);
                System.out.println("in testing");
                break;
            default: // just change the value in here to test
                currentSequence = autoSequences.get(3);
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
