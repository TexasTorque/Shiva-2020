package org.texastorque.auto;

import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.sequences.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.ArrayList;

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
        autoSequences.add(new VinayMode()); // Mode 0
        autoSequences.add(new Baseline()); // Baseline 
        autoSequences.add(new ShootButWorse()); // Deadshot 
        autoSequences.add(new ShootButNotAsBad()); // Liveshot
        autoSequences.add(new Testing()); // Testing 

        // SmartDashboard.putStringArray("AutoList", autoSequenceNames);
        // System.out.println(working);
        autoSelector.setDefaultOption("Mode 0", "Mode 0"); // vinaymode
        autoSelector.addOption("Baseline", "Baseline"); // baseline
        autoSelector.addOption("Deadshot", "Deadshot"); // shoot but worse
        autoSelector.addOption("Liveshot", "Liveshot"); // shoot but not as bad 
        autoSelector.addOption("Testing", "Testing"); // testing 

        SmartDashboard.putData("autos", autoSelector);
        System.out.println("All auto sequences loaded.");
    } // constructor

    public void displayChoices(){
        SmartDashboard.putData("autos", autoSelector);
        // SmartDashboard.putStringArray("AutoList", autoSequenceNames);
    } // display choices

    public void chooseSequence(){
        // String autoChoice = autoSelector.getSelected();
        // // String autoChoice = SmartDashboard.getString("AutoList", "null");
        // System.out.println(autoChoice);
        String autoChoice = NetworkTableInstance.getDefault().getTable("SmartDashboard").getSubTable("autos").getEntry("selected").getString("N/A");
        
        System.out.println("autos via network tables: " + autoChoice);

        switch(autoChoice){
            case "Mode 0": // mode 0
                currentSequence = autoSequences.get(0);
                break;
            case "Baseline": // baseline 
                currentSequence = autoSequences.get(1);
                break;
            case "Deadshot": // shoot but worse 
                currentSequence = autoSequences.get(2);
                break;
            case "Liveshot": // shoot but not as bad 
                currentSequence = autoSequences.get(3);
                break;
            case "Testing": // testing 
                currentSequence = autoSequences.get(4);
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
