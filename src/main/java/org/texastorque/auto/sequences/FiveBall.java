package org.texastorque.auto.sequences;

import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveDistance;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.DriveTurn;
import org.texastorque.auto.commands.DriveVision;
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
        block2.add(new DriveTurn(0,-130));
        // block2.add(new DrivePath(delay, points, forward)

        ArrayList<Command> block3 = new ArrayList<>();
        block3.add(new DriveDistance(0, 130));
        block3.add(new DriveTurn(3,-40));
        // intake magazine and intake intake at same time 

        ArrayList<Command> block4 = new ArrayList<>();
        // block4.add(new DriveVision(0,1.5));
        // block4.add(new VisionShoot(1.5, 3, 5550, 3));

        addBlock(block1);
        addBlock(block2);
        addBlock(block3);
        // addBlock(block4);
    }
    
}
