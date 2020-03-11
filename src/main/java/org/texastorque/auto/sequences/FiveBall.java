package org.texastorque.auto.sequences;

import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveDistance;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.DriveTurn;
import org.texastorque.auto.commands.DriveVision;
import org.texastorque.auto.commands.IntakeSet;
import org.texastorque.auto.commands.MagAutoLoad;
import org.texastorque.auto.commands.ShooterSet;
import org.texastorque.auto.commands.VisionShoot;
import org.texastorque.auto.Command;

import java.util.ArrayList;

public class FiveBall extends Sequence{

    @Override
    protected void init() {
        ArrayList<Command> block1 = new ArrayList<>();
        block1.add(new DriveVision(0, 1.5));
        block1.add(new VisionShoot(1.5, 3, 3000, 3));

        ArrayList<Command> block2 = new ArrayList<>();
        block2.add(new DriveTurn(0,-130)); // turn toward trench
        // block2.add(new DrivePath(delay, points, forward)

        ArrayList<Command> block3 = new ArrayList<>();
        block3.add(new DriveDistance(0, 130)); // drive to trench 
        block3.add(new DriveTurn(3,-40)); // turn into trench
        // block3.add(new IntakeSet(3, 2, 1));
        // intake magazine and intake intake at same time 

        ArrayList<Command> block4 = new ArrayList<>();
        block4.add(new MagAutoLoad()); // load magazine
        block4.add(new DriveDistance(0, 130)); // drive forward to pick up balls
        
        ArrayList<Command> block5 = new ArrayList<>();
        block5.add(new DriveTurn(0,-180)); // turn back to face vision target
        block5.add(new DriveVision(1, 2)); // align with vision target

        ArrayList<Command> block6 = new ArrayList<>();
        block6.add(new VisionShoot(0, 3, 5000, 3)); // shoot into target


        addBlock(block1);
        addBlock(block2);
        addBlock(block3);
        // addBlock(block4);
        // addBlock(block5);
        // addBlock(block6);
    }
    
}
