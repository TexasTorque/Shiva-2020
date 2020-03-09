package org.texastorque.auto.sequences;

import java.util.ArrayList;

import org.texastorque.auto.Command;
import org.texastorque.auto.Sequence;
import org.texastorque.auto.commands.DriveDistance;
import org.texastorque.auto.commands.DrivePath;
import org.texastorque.auto.commands.DriveTurn;
import org.texastorque.auto.commands.MagazineSet;
import org.texastorque.auto.commands.ShooterSet;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class Testing extends Sequence {

    @Override
    protected void init() {
        // ArrayList<Command> block1 = new ArrayList<>();
        // Waypoint[] points = new Waypoint[] {
        //     new Waypoint(0,0,0),
        //     new Waypoint(0,-3,0)
        // };
        // block1.add(new DrivePath(0, points, true));

        // addBlock(block1);

        // ArrayList<Command> block1 = new ArrayList<>();
        // block1.add(new DriveTurn(0, 150));

        // addBlock(block1);
    }
}
