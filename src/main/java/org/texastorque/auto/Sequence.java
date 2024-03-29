package org.texastorque.auto;

import edu.wpi.first.wpilibj.Timer;
import java.util.ArrayList;

public abstract class Sequence {
    private ArrayList<ArrayList<Command>> sequence;
    private boolean started;
    private boolean ended;
    private double startTime;
    private int blockIndex;

    protected Sequence(){
        sequence = new ArrayList<ArrayList<Command>>();
        started = false;
        ended = false;
        blockIndex = 0;
        init();
    } // constructor

    protected abstract void init();

    protected void addBlock(ArrayList<Command> block){
        sequence.add(block);
    }

    public void run(){
        if (!started){
            startTime = Timer.getFPGATimestamp();
            started = true;
            System.out.println("Starting sequence: " + sequence);
        }

        if (blockIndex < sequence.size()) {
            boolean blockEnded = true;
            double currentTime = Timer.getFPGATimestamp();
            for (Command command : sequence.get(blockIndex)) {
                if (currentTime - startTime > command.getDelay()) {
                    if (!command.run()) {
                        blockEnded = false;
                    }
                } else {
                    blockEnded = false;
                }
            } // go through each command in the block

            if (blockEnded) {
                blockIndex++;
                startTime = Timer.getFPGATimestamp();
                System.out.println("Block " + blockIndex + " ended.");
            }
        } // if there are still blocks to be run
        else if (!ended){
            System.out.println("Auto sequences done.");
            ended = true;
        }
    } // run

    public boolean hasEnded(){
            return ended;
        } // has ended

        public void reset(){
            blockIndex = 0;
            started = false;
            ended = false;
        } // reset 
} // Sequence
