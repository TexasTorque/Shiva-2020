package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.DriveTime;
import org.texastorque.auto.commands.DriveVision;
import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.commands.ShooterSet;
import org.texastorque.auto.commands.VisionShoot;

import jaci.pathfinder.Waypoint;

public class BarrelMode extends Sequence {

    @Override
    protected void init() {
        ArrayList<Command> block1 = new ArrayList<>();
        Waypoint[] points = new Waypoint[] {
            new Waypoint(0,0,0),
            new Waypoint(0,-3,0)
        };
        block1.add(new DrivePath(0, points, true));
        //block1.add(new DriveVision(0, 3));

        ArrayList<Command> block2 = new ArrayList<>();
        block2.add(new DriveTime(0, 0.5, -0.3));
        // block2.add(new VisionShoot(0, 5, 5250, 3));
        //block2.add(new ShooterSet(0, 5250, 4, 3));
        //block2.add(new MagazineSet(2, 0.25, true, 3, true, 3, true, 3.25));


        addBlock(block1);
        addBlock(block2);
    }
}
